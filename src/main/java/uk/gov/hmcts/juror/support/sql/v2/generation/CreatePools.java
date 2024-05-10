package uk.gov.hmcts.juror.support.sql.v2.generation;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorWeightedImpl;
import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.sql.v1.entity.Juror;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorPoolRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorRepository;
import uk.gov.hmcts.juror.support.sql.v2.DataCreator;
import uk.gov.hmcts.juror.support.sql.v2.flows.CreatePool;
import uk.gov.hmcts.juror.support.sql.v2.flows.enums.PoolType;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolCreateRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.CreatePoolControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.CourtDetails;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.User;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;
import uk.gov.hmcts.juror.support.sql.v2.support.RollingList;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreatePools {
    private static final RandomFromCollectionGeneratorImpl<LocalDate> poolStartDatesGen;
    private static final List<LocalDate> poolDates;
    private static final RandomFromCollectionGeneratorWeightedImpl<PoolType> poolTypeGenerator =
        new RandomFromCollectionGeneratorWeightedImpl<>(Map.of(
            PoolType.CRO, 0.85,
            PoolType.CIV, 0.10,
            PoolType.HGH, 0.05
        ));

    static {
        poolDates = new ArrayList<>();
        poolDates.addAll(
            List.of(
                LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(1).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(1).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(2).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(3).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(4).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(5).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(6).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(7).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(8).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),

                LocalDate.now().minusWeeks(1).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().minusWeeks(2).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().minusWeeks(3).with(TemporalAdjusters.next(DayOfWeek.MONDAY))
            ));
        poolStartDatesGen = new RandomFromCollectionGeneratorImpl<>(poolDates);
    }

    private final JurorPoolRepository jurorPoolRepository;
    private final JurorRepository jurorRepository;
    private final PlatformTransactionManager transactionManager;
    private final Summons summons;
    private TransactionTemplate transactionTemplate;

    @PostConstruct
    public void init() {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public void createPools() {
        //Create deferral pools
        if (DataCreator.ENV.isCreateDeferralPools()) {
            createDeferralPools();
        }

        log.info("Creating Bureau Pools");

        for (int index = 0; index < DataCreator.ENV.getTotalPool(); index++) {
            try {
                log.info("Creating pool with index: " + index);
                CourtDetails courtLoc = DataCreator.ENV.getCourts().get(index % DataCreator.ENV.getCourts().size());
                String locCode = new RandomFromCollectionGeneratorImpl<>(courtLoc.getLocCodes()).generate();

                User user = DataCreator.ENV.getRandomBureauUser();
                PoolRequestDto poolRequestDto = CreatePool.createPoolOnBehalfOfCourt(
                    user,
                    locCode,
                    LocalDateTime.of(poolStartDatesGen.generate(), LocalTime.of(8, 30)),
                    poolTypeGenerator.generate(),
                    false,
                    (int) Math.round(DataCreator.ENV.getNumberOfJurorsPerPool() * 0.60),
                    0
                );
                //5% change to not summon jurors
                if (RandomGenerator.nextInt(0, 100) > 5) {
                    summons.summonJurors(user, courtLoc, poolRequestDto);
                }
            } catch (Throwable e) {
                log.error("Error creating pool", e);
            }
        }
    }

    private void createDeferralPools() {
        log.info("Creating deferral Pools");
        for (CourtDetails courtLoc : DataCreator.ENV.getCourts()) {
            List<String> postcodes = courtLoc.getPostcodes();
            for (String locCode : courtLoc.getLocCodes()) {
                User user = DataCreator.ENV.getRandomBureauUser();
                PoolRequestDto poolRequestDto = CreatePool.createPoolOnBehalfOfCourt(
                    user,
                    locCode,
                    LocalDateTime.of(poolStartDatesGen.generate(), LocalTime.of(1, 30)),
                    poolTypeGenerator.generate(),
                    false,
                    DataCreator.ENV.getNumberOfJurorsPerPool() * 2,
                    0
                );


                new CreatePoolControllerClient()
                    .createPoolRequest(
                        jurorPoolRepository,
                        new JwtDetailsBureau(user),
                        PoolCreateRequestDto.builder()
                            .poolNumber(poolRequestDto.getPoolNumber())
                            .startDate(poolRequestDto.getAttendanceDate())
                            .attendTime(
                                LocalDateTime.of(poolRequestDto.getAttendanceDate(),
                                    poolRequestDto.getAttendanceTime()))
                            .noRequested(DataCreator.ENV.getNumberOfJurorsPerPool() * 2)
                            .bureauDeferrals(DataCreator.ENV.getNumberOfDeferrals())
                            .numberRequired(DataCreator.ENV.getNumberOfJurorsPerPool())
                            .citizensToSummon(DataCreator.ENV.getNumberOfJurorsPerPool() * 2)
                            .catchmentArea(poolRequestDto.getLocationCode())
                            .postcodes(postcodes)
                            .build());

                List<JurorPool> jurorPools =
                    transactionTemplate.execute(
                        status -> jurorPoolRepository.findAllByPoolNumber(poolRequestDto.getPoolNumber()));

                RollingList<LocalDate> attendanceDates = new RollingList<>(poolDates);
                for (JurorPool jurorPool : jurorPools) {
                    Juror juror = transactionTemplate.execute(
                        status -> jurorRepository.findById(jurorPool.getJurorNumber()).orElseThrow());
                    summons.deferralSummon(user, jurorPool, juror, attendanceDates.getNext());
                }
            }
        }
    }
}


