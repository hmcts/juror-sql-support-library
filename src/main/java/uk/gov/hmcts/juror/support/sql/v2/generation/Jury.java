package uk.gov.hmcts.juror.support.sql.v2.generation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import uk.gov.hmcts.juror.support.generation.generators.code.Generator;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorWeightedImpl;
import uk.gov.hmcts.juror.support.sql.v1.Util;
import uk.gov.hmcts.juror.support.sql.v1.entity.Juror;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorRepository;
import uk.gov.hmcts.juror.support.sql.v2.DataCreator;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.JurorDetailRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.JurorListRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial.TrialSummaryDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.trial.PanelResult;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.trial.PanelControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.User;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.JurorTrialWithTrial;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.Trial;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.JurorTrialWithTrialRepository;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.TrialRepository;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Slf4j
@Service
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Jury {

    private final TransactionTemplate transactionTemplate;
    private final JurorRepository jurorRepository;
    private final TrialRepository trialRepository;
    private final JurorTrialWithTrialRepository jurorTrialWithTrialRepository;
    @Autowired @Lazy
    private CreateTrials createTrials;

    @Autowired
    public Jury(PlatformTransactionManager transactionManager, JurorRepository jurorRepository,
                TrialRepository trialRepository, JurorTrialWithTrialRepository jurorTrialWithTrialRepository) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.jurorRepository = jurorRepository;
        this.trialRepository = trialRepository;
        this.jurorTrialWithTrialRepository = jurorTrialWithTrialRepository;
    }

    public final Generator<PanelResult> panelResultRejectGen = new RandomFromCollectionGeneratorWeightedImpl<>(
        Map.of(
            PanelResult.NOT_USED, 0.40,
            PanelResult.CHALLENGED, 0.60
        ));


    public List<Juror> empanelledJury(User user, TrialSummaryDto trialSummaryDto,
                                      LocalDate date,
                                      String locCode,
                                      List<JurorPool> jurorPools, int numberOfJurorsWanted) {
        final List<Juror> jurors = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(0);

        BiFunction<JurorPool, Juror, PanelResult> getPanelResult = (jurorPool, juror) -> {
            if (count.incrementAndGet() <= numberOfJurorsWanted) {
                jurors.add(juror);
                return PanelResult.JUROR;
            }
            return panelResultRejectGen.generate();
        };
        new PanelControllerClient()
            .processEmpanelled(
                new JwtDetailsBureau(user),
                JurorListRequestDto.builder()
                    .jurors(
                        jurorPools.stream()
                            .map(jurorPool -> {
                                Juror juror =
                                    transactionTemplate.execute(
                                        status -> jurorRepository.findById(jurorPool.getJurorNumber())
                                            .orElseThrow());
                                assert juror != null;
                                return JurorDetailRequestDto.builder()
                                    .jurorNumber(jurorPool.getJurorNumber())
                                    .firstName(juror.getFirstName())
                                    .lastName(juror.getLastName())
                                    .result(getPanelResult.apply(jurorPool, juror))
                                    .build();
                            })
                            .collect(Collectors.toList())
                    )
                    .trialNumber(trialSummaryDto.getTrialNumber())
                    .courtLocationCode(locCode)
                    .numberRequested(Math.min(numberOfJurorsWanted, jurorPools.size()))
                    .attendanceDate(date)
                    .build()
            );
        return jurors;
    }

    public void completeJurorsStatusJuror() {
        log.info("Completing juror status for jurors in jury");
        List<DataCreator.JurorNumberPoolNumber> pools = new ArrayList<>();
        String lastTrialNumber = null;

        List<Runnable> tasks = new ArrayList<>();

        int count = 0;
        for (JurorTrialWithTrial jurorTrialWithTrial : jurorTrialWithTrialRepository.findJurorTrialsJuryNotEnded()) {
            if (!jurorTrialWithTrial.getTrialNumber().equals(lastTrialNumber)) {
                count++;
                log.info("Setup trial: " + count);
                if (lastTrialNumber != null) {
                    String finalLastTrialNumber = lastTrialNumber;
                    Trial trial = trialRepository.findByTrialNumber(lastTrialNumber);
                    List<DataCreator.JurorNumberPoolNumber> finalPools = pools;
                    tasks.add(() ->
                        createTrials.processTrialJurorPools(finalLastTrialNumber,
                            trial.getTrialStartDate(),
                            trial.getLocCode(),
                            finalPools));
                }
                pools = new ArrayList<>();
                lastTrialNumber = jurorTrialWithTrial.getTrialNumber();
            }
            //TODO
//            pools.add(new DataCreator.JurorNumberPoolNumber(
//                jurorTrialWithTrial.getJurorNumber(),
//                jurorTrialWithTrial.getPoolNumber()));
        }

        AtomicLong processedCount = new AtomicLong(0);
        long totalCOunt = tasks.size();
        tasks.parallelStream()
            .forEach(runnable -> {
                log.info("Processing trial: " + processedCount.incrementAndGet() + " of " + totalCOunt);
                Util.retryElseThrow(runnable, 1, false);
            });

    }
}
