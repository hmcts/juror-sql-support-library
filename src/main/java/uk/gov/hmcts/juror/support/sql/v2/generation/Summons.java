package uk.gov.hmcts.juror.support.sql.v2.generation;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorWeightedImpl;
import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.sql.v1.Util;
import uk.gov.hmcts.juror.support.sql.v1.entity.Juror;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorPoolRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorRepository;
import uk.gov.hmcts.juror.support.sql.v2.DataCreator;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.juror.controller.request.JurorResponseDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.JurorPaperResponseDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolCreateRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.ReplyMethod;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.CreatePoolControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.JurorPaperResponseControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.PublicEndpointControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.CourtDetails;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.User;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsPublic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Summons {

    private final JurorPoolRepository jurorPoolRepository;
    private final JurorRepository jurorRepository;
    private final ProcessReply processReply;
    private final JurorResponse jurorResponse;

    private final PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;

    @PostConstruct
    public void init() {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public void summonJurors(User user, CourtDetails courtDetails, PoolRequestDto poolRequestDto) {
        try {
            log.info("Summoning jurors for pool number {}", poolRequestDto.getPoolNumber());
            List<String> postCodes = courtDetails.getPostcodes();
            Long availableDeferrals = new CreatePoolControllerClient()
                .getBureauDeferrals(
                    new JwtDetailsBureau(user),
                    poolRequestDto.getLocationCode(),
                    poolRequestDto.getAttendanceDate()
                );

            int numberOfDeferrals = DataCreator.ENV.getNumberOfDeferrals();
            if (availableDeferrals < numberOfDeferrals) {
                numberOfDeferrals = availableDeferrals.intValue();
            }

            int numToSummonExcludingDeferrals = DataCreator.ENV.getNumberOfJurorsPerPool() - numberOfDeferrals;

            new CreatePoolControllerClient()
                .createPoolRequest(
                    jurorPoolRepository,
                    new JwtDetailsBureau(user),
                    PoolCreateRequestDto.builder()
                        .poolNumber(poolRequestDto.getPoolNumber())
                        .startDate(poolRequestDto.getAttendanceDate())
                        .attendTime(
                            LocalDateTime.of(poolRequestDto.getAttendanceDate(), poolRequestDto.getAttendanceTime()))
                        .noRequested(DataCreator.ENV.getNumberOfJurorsPerPool())
                        .bureauDeferrals(numberOfDeferrals)
                        .numberRequired((int) Math.round(DataCreator.ENV.getNumberOfJurorsPerPool() * 0.50))
                        .citizensToSummon(numToSummonExcludingDeferrals)
                        .catchmentArea(poolRequestDto.getLocationCode())
                        .postcodes(postCodes)
                        .build());
            List<JurorPool> jurorPools =
                transactionTemplate.execute(
                    status -> jurorPoolRepository.findAllByPoolNumber(poolRequestDto.getPoolNumber()));
            enterSummonsReply(user, jurorPools);
        } catch (Throwable e) {
            log.error("Error summoning jurors", e);
        }
    }


    private void enterSummonsReply(User user, List<JurorPool> jurorPools) {
        for (JurorPool jurorPool : jurorPools) {
            try {
                Juror juror = transactionTemplate.execute(
                    status -> jurorRepository.findById(jurorPool.getJurorNumber()).orElseThrow());

                if (juror.isResponded()) {
                    continue;
                }
                log.info("Processing juror {}. Pool {}", juror.getJurorNumber(), jurorPool.getPoolNumber());

                RandomFromCollectionGeneratorWeightedImpl<Runnable> summonMethodGenerator =
                    new RandomFromCollectionGeneratorWeightedImpl<>(Map.of(
                        () -> straightForwardSummon(user, jurorPool, juror), 0.75,
                        () -> ineligibleSummon(user, jurorPool, juror), 0.05,
                        () -> deferralSummon(user, jurorPool, juror,
                            Util.getFutureDate(DataCreator.ENV.getPoolDates(), jurorPool.getNextDate())),
                        0.05,
                        () -> excusalSummon(user, jurorPool, juror), 0.05,
                        () -> disqualifySummon(user, jurorPool, juror), 0.05,
                        () -> log.info("No action taken"), 0.05
                    ));
                summonMethodGenerator.generate().run();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Error processing juror {}. Pool {}",
                    jurorPool.getJurorNumber(),
                    jurorPool.getPoolNumber(),
                    e);
            }
        }
    }

    private void ineligibleSummon(User user, JurorPool jurorPool, Juror juror) {
        if (RandomGenerator.nextDouble(0, 1) > DataCreator.ENV.getPaperChance()) {
            ineligibleSummonPaper(user, jurorPool, juror);
        } else {
            ineligibleSummonDigital(user, jurorPool, juror);
        }
    }

    private void ineligibleSummonDigital(User user, JurorPool jurorPool, Juror juror) {
        JurorResponseDto jurorResponseDto =
            jurorResponse.getValidJurorResponseDto(juror);
        int random = RandomGenerator.nextInt(0, 32);

        jurorResponseDto.setQualify(JurorResponseDto.Qualify.builder()
            .onBail(
                JurorResponseDto.Qualify.Answerable.builder()
                    .answer(random % 2 == 0)
                    .details(random % 2 == 0 ? "Some Details 1" : null)
                    .build())
            .mentalHealthAct(JurorResponseDto.Qualify.Answerable.builder()
                .answer(random % 3 == 0)
                .details(random % 3 == 0 ? "Some Details 2" : null)
                .build())
            .livedConsecutive(
                JurorResponseDto.Qualify.Answerable.builder()
                    .answer(random % 3 == 0)
                    .details(random % 3 == 0 ? "Some Details 3" : null)
                    .build())
            .convicted(JurorResponseDto.Qualify.Answerable.builder()
                .answer(random % 4 == 0)
                .details(random % 4 == 0 ? "Some Details 4" : null)
                .build())
            .build());

        new PublicEndpointControllerClient()
            .respondToSummons(
                new JwtDetailsPublic(juror),
                jurorResponseDto);
        processReply.processReplyDisqualify(user, jurorPool, ReplyMethod.DIGITAL);
    }

    private void ineligibleSummonPaper(User user, JurorPool jurorPool, Juror juror) {
        JurorPaperResponseDto jurorPaperResponseDto =
            jurorResponse.getValidJurorPaperResponseDto(jurorPool, juror);
        int random = RandomGenerator.nextInt(0, 32);
        jurorPaperResponseDto.setEligibility(JurorPaperResponseDto.Eligibility.builder()
            .onBail(random % 2 == 0)
            .livedConsecutive(random % 3 == 0)
            .convicted(random % 4 == 0)
            .mentalHealthAct(random % 5 == 0)
            .mentalHealthCapacity(random % 6 == 0)
            .build());

        new JurorPaperResponseControllerClient()
            .respondToSummons(
                new JwtDetailsBureau(user),
                jurorPaperResponseDto);

        processReply.processReplyDisqualify(user, jurorPool, ReplyMethod.PAPER);
    }

    public void deferralSummon(User user, JurorPool jurorPool, Juror juror,
                               LocalDate deferralDate) {
        if (RandomGenerator.nextDouble(0, 1) > DataCreator.ENV.getPaperChance()) {
            deferralSummonPaper(user, jurorPool, juror, deferralDate);
        } else {
            deferralSummonDigital(user, jurorPool, juror, deferralDate);
        }
    }


    private void straightForwardSummon(User user, JurorPool jurorPool, Juror juror) {
        if (RandomGenerator.nextDouble(0, 1) > DataCreator.ENV.getPaperChance()) {//TODO
            straightForwardSummonPaper(user, jurorPool, juror);
        } else {
            straightForwardSummonDigital(user, jurorPool, juror);
        }
    }

    private void straightForwardSummonDigital(User user, JurorPool jurorPool, Juror juror) {
        new PublicEndpointControllerClient()
            .respondToSummons(
                new JwtDetailsPublic(juror),
                jurorResponse.getValidJurorResponseDto(juror));

        Juror juror1 =
            transactionTemplate.execute(status -> jurorRepository.findById(juror.getJurorNumber()).orElseThrow());
        if (juror1.isResponded()) {
            return;
        }
        processReply.processReplyRespondedDigital(user, jurorPool);
    }


    private void straightForwardSummonPaper(User user, JurorPool jurorPool, Juror juror) {
        new JurorPaperResponseControllerClient()
            .respondToSummons(
                new JwtDetailsBureau(user),
                jurorResponse.getValidJurorPaperResponseDto(jurorPool, juror));
        processReply.processReplyResponded(user, jurorPool);
    }


    private void disqualifySummon(User user, JurorPool jurorPool, Juror juror) {
        if (RandomGenerator.nextDouble(0, 1) > DataCreator.ENV.getPaperChance()) {
            disqualifySummonPaper(user, jurorPool, juror);
        } else {
            disqualifySummonDigital(user, jurorPool, juror);
        }
    }

    private void disqualifySummonPaper(User user, JurorPool jurorPool, Juror juror) {
        new JurorPaperResponseControllerClient()
            .respondToSummons(
                new JwtDetailsBureau(user),
                jurorResponse.getValidJurorPaperResponseDto(jurorPool, juror));
        processReply.processReplyDisqualify(user, jurorPool, ReplyMethod.PAPER);
    }

    private void disqualifySummonDigital(User user, JurorPool jurorPool, Juror juror) {
        if (!DataCreator.ENV.isProcessReply()) {
            return;
        }
        new PublicEndpointControllerClient()
            .respondToSummons(
                new JwtDetailsPublic(juror),
                jurorResponse.getValidJurorResponseDto(juror));
        processReply.processReplyDisqualify(user, jurorPool, ReplyMethod.DIGITAL);
    }


    private void excusalSummon(User user, JurorPool jurorPool, Juror juror) {
        if (RandomGenerator.nextDouble(0, 1) > DataCreator.ENV.getPaperChance()) {
            excusalSummonPaper(user, jurorPool, juror);
        } else {
            excusalSummonDigital(user, jurorPool, juror);
        }
    }

    private void excusalSummonDigital(User user, JurorPool jurorPool, Juror juror) {
        JurorResponseDto jurorResponseDto = jurorResponse.getValidJurorResponseDto(juror);
        jurorResponseDto.setExcusal(JurorResponseDto.Excusal.builder()
            .reason("I am busy this day")
            .build());

        new PublicEndpointControllerClient()
            .respondToSummons(
                new JwtDetailsPublic(juror),
                jurorResponseDto);
        processReply.processReplyExcusal(user, jurorPool, ReplyMethod.DIGITAL);
    }

    private void excusalSummonPaper(User user, JurorPool jurorPool, Juror juror) {
        JurorPaperResponseDto jurorPaperResponseDto =
            jurorResponse.getValidJurorPaperResponseDto(jurorPool, juror);
        jurorPaperResponseDto.setExcusal(true);
        new JurorPaperResponseControllerClient()
            .respondToSummons(new JwtDetailsBureau(user), jurorPaperResponseDto);
        processReply.processReplyExcusal(user, jurorPool, ReplyMethod.PAPER);
    }

    private void deferralSummonPaper(User user, JurorPool jurorPool, Juror juror, LocalDate deferralDate) {
        JurorPaperResponseDto jurorPaperResponseDto =
            jurorResponse.getValidJurorPaperResponseDto(jurorPool, juror);
        jurorPaperResponseDto.setDeferral(true);
        new JurorPaperResponseControllerClient()
            .respondToSummons(new JwtDetailsBureau(user), jurorPaperResponseDto);
        processReply.processReplyDeferral(user, jurorPool, ReplyMethod.PAPER, deferralDate);
    }

    private void deferralSummonDigital(User user, JurorPool jurorPool, Juror juror, LocalDate deferralDate) {
        JurorResponseDto jurorResponseDto = jurorResponse.getValidJurorResponseDto(juror);
        jurorResponseDto.setDeferral(JurorResponseDto.Deferral.builder()
            .dates(deferralDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .reason("I am busy this day")
            .build());

        new PublicEndpointControllerClient()
            .respondToSummons(
                new JwtDetailsPublic(juror),
                jurorResponseDto);
        processReply.processReplyDeferral(user, jurorPool, ReplyMethod.DIGITAL, deferralDate);
    }


}
