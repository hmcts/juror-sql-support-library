package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.util.Map;

public class TrialControllerClient extends BaseClient {

    public TrialControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    public java.lang.Void endTrial(JwtDetailsBureau jwtDetailsBureau,
                                   uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.EndTrialDto endTrialDto) {
        return triggerApi(
            HttpMethod.PATCH,
            "/api/v1/moj/trial/end-trial",
            endTrialDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.Void returnJury(JwtDetailsBureau jwtDetailsBureau,
                                     java.lang.String trialNumber,
                                     java.lang.String locationCode,
                                     uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.ReturnJuryDto returnJuryDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/trial/return-jury",
            returnJuryDto,
            Map.of("trialNumber", String.valueOf(trialNumber),
                "locationCode", String.valueOf(locationCode)),
            Map.of("location_code", String.valueOf(locationCode),
                "trial_number", String.valueOf(trialNumber)),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial.TrialSummaryDto createTrial(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.TrialDto trialDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/trial/create",
            trialDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.PageDto<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial.TrialListDto> getTrials(
        JwtDetailsBureau jwtDetailsBureau,
        int pageNumber,
        java.lang.String sortBy,
        java.lang.String sortOrder,
        java.lang.String trialNumber,
        java.lang.Boolean isActive) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/trial/list",
            null,
            Map.of("pageNumber", String.valueOf(pageNumber),
                "sortOrder", String.valueOf(sortOrder),
                "sortBy", String.valueOf(sortBy),
                "isActive", String.valueOf(isActive)),
            Map.of("page_number", String.valueOf(pageNumber),
                "is_active", String.valueOf(isActive),
                "trial_number", String.valueOf(trialNumber),
                "sort_by", String.valueOf(sortBy),
                "sort_order", String.valueOf(sortOrder)),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.Void returnPanel(JwtDetailsBureau jwtDetailsBureau,
                                      java.lang.String trialNumber,
                                      java.lang.String locationCode,
                                      java.util.List jurorDetailRequestDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/trial/return-panel",
            jurorDetailRequestDto,
            Map.of("trialNumber", String.valueOf(trialNumber),
                "locationCode", String.valueOf(locationCode)),
            Map.of("location_code", String.valueOf(locationCode),
                "trial_number", String.valueOf(trialNumber)),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial.TrialSummaryDto getTrialSummary(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String trialNumber,
        java.lang.String locationCode) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/trial/summary",
            null,
            Map.of("trialNumber", String.valueOf(trialNumber),
                "locationCode", String.valueOf(locationCode)),
            Map.of("location_code", String.valueOf(locationCode),
                "trial_number", String.valueOf(trialNumber)),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

}
