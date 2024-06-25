package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.util.Map;

public class JurorPaperResponseControllerClient extends BaseClient {

    public JurorPaperResponseControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    public void updateJurorSignatureDetails(JwtDetailsBureau jwtDetailsBureau,
                                            uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.SignatureDetailsDto signatureDetailsDto,
                                            java.lang.String jurorNumber) {
        triggerApiVoidReturn(
            HttpMethod.PATCH,
            "/api/v1/moj/juror-paper-response/juror/{jurorNumber}/details/signature",
            signatureDetailsDto,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau);
    }

    public void updatePaperSpecialNeedsDetails(JwtDetailsBureau jwtDetailsBureau,
                                               uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.ReasonableAdjustmentDetailsDto reasonableAdjustmentDetailsDto,
                                               java.lang.String jurorNumber) {
        triggerApiVoidReturn(
            HttpMethod.PATCH,
            "/api/v1/moj/juror-paper-response/juror/{jurorNumber}/details/special-needs",
            reasonableAdjustmentDetailsDto,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau);
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.JurorPaperResponseDetailDto retrieveJurorById(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String jurorNumber) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/juror-paper-response/juror/{jurorNumber}",
            null,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public void updatePaperSummonsCjsDetails(JwtDetailsBureau jwtDetailsBureau,
                                             uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.CjsEmploymentDetailsDto cjsEmploymentDetailsDto,
                                             java.lang.String jurorNumber) {
        triggerApiVoidReturn(
            HttpMethod.PATCH,
            "/api/v1/moj/juror-paper-response/juror/{jurorNumber}/details/cjs",
            cjsEmploymentDetailsDto,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau);
    }

    public void updateJurorEligibilityDetails(JwtDetailsBureau jwtDetailsBureau,
                                              uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.EligibilityDetailsDto eligibilityDetailsDto,
                                              java.lang.String jurorNumber) {
        triggerApiVoidReturn(
            HttpMethod.PATCH,
            "/api/v1/moj/juror-paper-response/juror/{jurorNumber}/details/eligibility",
            eligibilityDetailsDto,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau);
    }

    public java.lang.Void updateJurorPaperResponseStatus(JwtDetailsBureau jwtDetailsBureau,
                                                         java.lang.String jurorNumber,
                                                         uk.gov.hmcts.juror.support.sql.v2.generated.api.juror.domain.ProcessingStatus processingStatus) {
        return triggerApi(
            HttpMethod.PUT,
            "/api/v1/moj/juror-paper-response/update-status/{jurorNumber}/{processingStatus}",
            null,
            Map.of("jurorNumber", String.valueOf(jurorNumber),
                "processingStatus", String.valueOf(processingStatus)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public void updateJurorReplyTypeDetails(JwtDetailsBureau jwtDetailsBureau,
                                            uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.ReplyTypeDetailsDto replyTypeDetailsDto,
                                            java.lang.String jurorNumber) {
        triggerApiVoidReturn(
            HttpMethod.PATCH,
            "/api/v1/moj/juror-paper-response/juror/{jurorNumber}/details/reply-type",
            replyTypeDetailsDto,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau);
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.summonsmanagement.SaveJurorPaperReplyResponseDto respondToSummons(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.JurorPaperResponseDto paperResponseDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/juror-paper-response/response",
            paperResponseDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

}
