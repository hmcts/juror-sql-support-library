package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.util.Map;

public class AdministrationJudgeControllerClient extends BaseClient {

    public AdministrationJudgeControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.administration.JudgeDetailsDto deleteJudge(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.Long judgeId) {
        return triggerApi(
            HttpMethod.DELETE,
            "/api/v1/moj/administration/judges/{judge_id}",
            null,
            Map.of("judge_id", String.valueOf(judgeId)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.administration.JudgeDetailsDto> viewAllJudgeDetails(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.Boolean isActive) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/administration/judges",
            null,
            Map.of(),
            Map.of("is_active", String.valueOf(isActive)),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.Void updateJudgeDetails(JwtDetailsBureau jwtDetailsBureau,
                                             java.lang.Long judgeId,
                                             uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.administration.JudgeUpdateDto judgeUpdateDto) {
        return triggerApi(
            HttpMethod.PUT,
            "/api/v1/moj/administration/judges/{judge_id}",
            judgeUpdateDto,
            Map.of("judge_id", String.valueOf(judgeId)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.Void createJudgeDetails(JwtDetailsBureau jwtDetailsBureau,
                                             uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.administration.JudgeCreateDto judgeCreateDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/administration/judges",
            judgeCreateDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.administration.JudgeDetailsDto viewJudgeDetails(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.Long judgeId) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/administration/judges/{judge_id}",
            null,
            Map.of("judge_id", String.valueOf(judgeId)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

}
