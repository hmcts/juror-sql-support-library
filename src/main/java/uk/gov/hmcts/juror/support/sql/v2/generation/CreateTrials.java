package uk.gov.hmcts.juror.support.sql.v2.generation;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.juror.support.generation.generators.code.Generator;
import uk.gov.hmcts.juror.support.generation.generators.value.FirstNameGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LastNameGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorWeightedImpl;
import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.sql.v1.Util;
import uk.gov.hmcts.juror.support.sql.v1.entity.Juror;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorPoolRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.PoolRequestRepository;
import uk.gov.hmcts.juror.support.sql.v2.DataCreator;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.CreatePanelDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.EndTrialDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.JurorDetailRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.ReturnJuryDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.TrialDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial.TrialSummaryDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.AppearanceStage;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.trial.PanelResult;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.trial.TrialType;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.TrialControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.trial.PanelControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.CourtDetails;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.User;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.AppearanceRepository;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreateTrials {

    private final AtomicLong caseIndex = new AtomicLong(1);
    private final Generator<TrialType> trialTypeGenerator =
        new RandomFromCollectionGeneratorImpl<>(TrialType.values());

    private static final Generator<String> nameGenerator = new FirstNameGeneratorImpl().addPostGenerate(string ->
        string + " " + new LastNameGeneratorImpl().generate());

    private final JurorPoolRepository jurorPoolRepository;
    private final PoolRequestRepository poolRequestRepository;
    private final Appearance appearance;
    private final Jury jury;
    private final CompleteService completeService;
    private final CreateExpenses createExpenses;
    private final AppearanceRepository appearanceRepository;
    private final JurorRepository jurorRepository;

    public void createTrials() {
        log.info("Creating Trials");
        int maxPerTrial = 16;
        //Only get responded jurors
        List<List<JurorPool>> jurorPoolsForTrials = new ArrayList<>();
        String lastCourt = "";
        List<JurorPool> tmpList = new ArrayList<>();
        Generator<Boolean> skipTrailGen = new RandomFromCollectionGeneratorWeightedImpl<>(Map.of(
            true, 0.90,
            false, 0.10
        ));
        for (JurorPool jurorPool : jurorPoolRepository.findJurorPoolsByStatusOrdered(2)) {
            String newLocCode = jurorPool.getLocCode(poolRequestRepository);
            if (!lastCourt.equals(newLocCode) || tmpList.size() >= maxPerTrial) {
                jurorPoolsForTrials.add(tmpList);
                tmpList = new ArrayList<>();
            }
            if (skipTrailGen.generate()) {
                tmpList.add(jurorPool);
            }
            lastCourt = newLocCode;
        }
        log.info("Trials: " + jurorPoolsForTrials.size());
        jurorPoolsForTrials
            .stream() //TODO make parallel
            .filter(list -> !list.isEmpty())
            .forEach(jurorPools -> Util.retryElseThrow(() -> createTrial(jurorPools), 2, false));
        log.info("Trials Creation finished");
    }

    @SneakyThrows
    public void createTrial(List<JurorPool> jurorPools) {
        try {
            JurorPool firstPool = jurorPools.get(0);
            String locCode = firstPool.getLocCode(poolRequestRepository);
            LocalDate nextDate = firstPool.getNextDate();//TODO random

            LocalDate trialEnd = nextDate.plusDays(RandomGenerator.nextInt(5, 15));
            if (trialEnd.getDayOfWeek() == DayOfWeek.SATURDAY) {
                trialEnd = trialEnd.minusDays(1);
            } else if (trialEnd.getDayOfWeek() == DayOfWeek.SUNDAY) {
                trialEnd = trialEnd.plusDays(1);
            }

            CourtDetails courtLoc = DataCreator.ENV.getCourt(locCode);
            User courtUser = new RandomFromCollectionGeneratorImpl<>(courtLoc.getUsernames()).generate();
            courtUser.setActiveLocCode(locCode);
            long currentCaseIndex = caseIndex.getAndIncrement();
            log.info(
                "Creating trial for court: " + locCode + " on " + nextDate + " with " + jurorPools.size() + " jurors");
            log.info("Case Index: " + currentCaseIndex);
            TrialSummaryDto trialSummaryDto = new TrialControllerClient()
                .createTrial(new JwtDetailsBureau(courtUser),
                    TrialDto.builder()
                        .caseNumber("CASE" + locCode + "A" + currentCaseIndex)
                        .trialType(trialTypeGenerator.generate())
                        .defendant(createDefendant())
                        .startDate(nextDate)
                        .judgeId(new RandomFromCollectionGeneratorImpl<>(courtLoc.getJudges()).generate().getId())
                        .courtroomId(
                            new RandomFromCollectionGeneratorImpl<>(courtLoc.getCourtRooms(locCode)).generate().getId())
                        .courtLocation(locCode)
                        .protectedTrial(false)
                        .build()
                );

            Util.retryElseThrow(() -> appearance.checkIn(courtUser, locCode, nextDate, jurorPools), 2, true);

            new PanelControllerClient()
                .createPanel(
                    new JwtDetailsBureau(courtUser),
                    CreatePanelDto.builder()
                        .attendanceDate(nextDate)
                        .trialNumber(trialSummaryDto.getTrialNumber())
                        .numberRequested(jurorPools.size())
                        .courtLocationCode(locCode)
                        .build());

            List<Juror> jurors =
                jury.empanelledJury(courtUser, trialSummaryDto, nextDate,
                    locCode, jurorPools, 12);


            List<String> jurorNumbers = jurorPools.stream().map(JurorPool::getJurorNumber).collect(Collectors.toList());
            appearance.checkOut(courtUser, locCode, nextDate, jurorNumbers);
//TODO            appearance.confirm(courtUser, locCode, nextDate, jurorNumbers);
            //Added trial days
            appearance.addAttendances(courtUser, courtLoc, locCode, nextDate.plusDays(1), trialEnd, jurorPools);
            //Return jury

            new TrialControllerClient()
                .returnJury(
                    new JwtDetailsBureau(courtUser),
                    trialSummaryDto.getTrialNumber(),
                    locCode,
                    ReturnJuryDto.builder()
                        .checkIn("08:30")
                        .checkOut(null)
                        .completed(false)
                        .attendanceDate(trialEnd)
                        .jurors(jurors.stream().map(
                            juror -> JurorDetailRequestDto.builder()
                                .jurorNumber(juror.getJurorNumber())
                                .firstName(juror.getFirstName())
                                .lastName(juror.getLastName())
                                .result(PanelResult.JUROR)
                                .build()).toList())
                        .build()
                );

            //End trial
            new TrialControllerClient()
                .endTrial(
                    new JwtDetailsBureau(courtUser),
                    EndTrialDto.builder()
                        .trialEndDate(trialEnd)
                        .trialNumber(trialSummaryDto.getTrialNumber())
                        .locationCode(locCode)
                        .build()
                );

            //Complete service
//TODO            completeService.completeService(courtUser, jurorPools, trialEnd);
            //Add expenses
//TODO            for (JurorPool jurorPool : jurorPools) {
//                createExpenses.addExpensesByAppearance(courtUser, courtLoc, locCode,
//                    appearanceRepository.findAllByPoolNumberAndJurorNumberAndAppearanceStage(jurorPool.getPoolNumber(),
//                        jurorPool.getJurorNumber(),
//                        AppearanceStage.EXPENSE_ENTERED));
//            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error("WAITING: Error creating trial: " + throwable.getMessage());
//            Thread.sleep(Long.MAX_VALUE);
        }
    }

    public String createDefendant() {
        String name = nameGenerator.generate();
        if (name.length() > 16) {
            name = name.substring(0, 16);
        }
        return name;
    }

    public void processTrialJurorPools(String trialNumber, LocalDate trialStart,
                                       String locCode, List<DataCreator.JurorNumberPoolNumber> pools) {
        log.info("Completing juror status for trial: " + trialNumber);
        LocalDate trialEnd = trialStart.plusDays(RandomGenerator.nextInt(5, 15));
        if (trialEnd.getDayOfWeek() == DayOfWeek.SATURDAY) {
            trialEnd = trialEnd.minusDays(1);
        } else if (trialEnd.getDayOfWeek() == DayOfWeek.SUNDAY) {
            trialEnd = trialEnd.plusDays(1);
        }
        CourtDetails courtLoc = DataCreator.ENV.getCourt(locCode);
        User courtUser = new RandomFromCollectionGeneratorImpl<>(courtLoc.getUsernames()).generate();

        List<JurorPool> jurorPools = pools.stream()
            .map(jurorPoolNumber -> jurorPoolRepository.findByPoolNumberAndJurorNumber(
                jurorPoolNumber.getPoolNumber(),
                jurorPoolNumber.getJurorNumber()))
            .toList();
        appearance.addAttendances(courtUser, courtLoc,
            locCode,
            trialStart.plusDays(1), trialEnd, jurorPools);

        List<Juror> jurors =
            pools.stream().map(jurorPool -> jurorRepository.findById(jurorPool.getJurorNumber()).get()).toList();

        new TrialControllerClient()
            .returnJury(
                new JwtDetailsBureau(courtUser),
                trialNumber,
                locCode,
                ReturnJuryDto.builder()
                    .checkIn("08:30")
                    .checkOut(null)
                    .completed(false)
                    .attendanceDate(trialEnd)
                    .jurors(jurors.stream().map(
                        juror -> JurorDetailRequestDto.builder()
                            .jurorNumber(juror.getJurorNumber())
                            .firstName(juror.getFirstName())
                            .lastName(juror.getLastName())
                            .result(PanelResult.JUROR)
                            .build()).toList())
                    .build()
            );

        //End trial
        new TrialControllerClient()
            .endTrial(
                new JwtDetailsBureau(courtUser),
                EndTrialDto.builder()
                    .trialEndDate(trialEnd)
                    .trialNumber(trialNumber)
                    .locationCode(locCode)
                    .build()
            );
    }
}
