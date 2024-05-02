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
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorRepository;
import uk.gov.hmcts.juror.support.sql.v2.DataCreator;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.bureau.controller.request.BureauResponseStatusUpdateDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.juror.domain.ProcessingStatus;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.DeferralReasonRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.ExcusalDecisionDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.summonsmanagement.DisqualifyJurorDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.ExcusalDecision;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.DisqualifyCodeEnum;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.ReplyMethod;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.DeferralMaintenanceControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.DisqualifyJurorControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.ExcusalResponseControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.JurorPaperResponseControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.JurorResponseControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.User;
import uk.gov.hmcts.juror.support.sql.v2.support.ExcusalCodeEnum;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProcessReply {

    private final JurorRepository jurorRepository;
    private final PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;

    @PostConstruct
    public void init() {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public void processReplyDisqualify(User user, JurorPool jurorPool, ReplyMethod replyMethod) {
        if (!DataCreator.ENV.isProcessReply()) {
            return;
        }
        if (RandomGenerator.nextDouble(0, 1) > 0.98) {
            return;
        }
        if (Boolean.TRUE.equals(transactionTemplate.execute(
            status -> jurorRepository.findById(jurorPool.getJurorNumber()).orElseThrow().isResponded()))) {
            return;
        }
        new DisqualifyJurorControllerClient()
            .disqualifyJuror(new JwtDetailsBureau(user),
                jurorPool.getJurorNumber(),
                DisqualifyJurorDto.builder()
                    .replyMethod(replyMethod)
                    .code(new RandomFromCollectionGeneratorImpl<>(DisqualifyCodeEnum.values()).generate())
                    .build());
    }

    public void processReplyDeferral(User user, JurorPool jurorPool, ReplyMethod replyMethod,
                                     LocalDate deferralDate) {
        if (!DataCreator.ENV.isProcessReply()) {
            return;
        }
        if (RandomGenerator.nextDouble(0, 1) > 0.98) {
            return;
        }
        new DeferralMaintenanceControllerClient()
            .processJurorDeferral(new JwtDetailsBureau(user),
                jurorPool.getJurorNumber(),
                DeferralReasonRequestDto.builder()
                    .deferralDate(deferralDate)
                    .excusalReasonCode(
                        new RandomFromCollectionGeneratorImpl<>(ExcusalCodeEnum.values()).generate().getCode())
                    .replyMethod(replyMethod)
                    .build());
    }

    public void processReplyExcusal(User user, JurorPool jurorPool, ReplyMethod replyMethod) {
        if (!DataCreator.ENV.isProcessReply()) {
            return;
        }
        if (RandomGenerator.nextDouble(0, 1) > 0.98) {
            return;
        }
        new ExcusalResponseControllerClient()
            .respondToExcusalRequest(new JwtDetailsBureau(user),
                ExcusalDecisionDto.builder()
                    .excusalReasonCode(
                        new RandomFromCollectionGeneratorImpl<>(ExcusalCodeEnum.values()).generate().getCode())
                    .excusalDecision(new RandomFromCollectionGeneratorWeightedImpl<>(
                        Map.of(ExcusalDecision.GRANT, 0.95, ExcusalDecision.REFUSE, 0.05)).generate())
                    .replyMethod(replyMethod)
                    .build(),
                jurorPool.getJurorNumber());
    }

    public void processReplyResponded(User user, JurorPool jurorPool) {
        if (!DataCreator.ENV.isProcessReply()) {
            return;
        }
        if (RandomGenerator.nextDouble(0, 1) > 0.98) {
            return;
        }
        new JurorPaperResponseControllerClient()
            .updateJurorPaperResponseStatus(
                new JwtDetailsBureau(user),
                jurorPool.getJurorNumber(),
                ProcessingStatus.CLOSED
            );
    }

    public void processReplyRespondedDigital(User user, JurorPool jurorPool) {
        if (!DataCreator.ENV.isProcessReply()) {
            return;
        }
        if (RandomGenerator.nextDouble(0, 1) > 0.98) {
            return;
        }
        new JurorResponseControllerClient()
            .updateResponseStatus(
                new JwtDetailsBureau(user),
                jurorPool.getJurorNumber(),
                BureauResponseStatusUpdateDto.builder()
                    .jurorNumber(jurorPool.getJurorNumber())
                    .version(1)//TODO confirm
                    .status(ProcessingStatus.CLOSED)
                    .build()
            );
    }
}
