package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.util.Map;

public class DeferralMaintenanceControllerClient extends BaseClient {

    public DeferralMaintenanceControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    public java.lang.Void processJurorDeferral(JwtDetailsBureau jwtDetailsBureau,
                                               java.lang.String jurorNumber,
                                               uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.DeferralReasonRequestDto deferralReasonDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/deferral-maintenance/juror/defer/{jurorNumber}",
            deferralReasonDto,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.DeferralListDto getDeferralsByCourtLocationCode(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String courtLocationCode) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/deferral-maintenance/deferrals/{courtLocationCode}",
            null,
            Map.of("courtLocationCode", String.valueOf(courtLocationCode)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.DeferralOptionsDto getDeferralOptionsForDatesAndCourtLocation(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String jurorNumber,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.DeferralDatesRequestDto deferralDatesRequestDto,
        java.lang.String locationCode) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/deferral-maintenance/available-pools/{locationCode}/{jurorNumber}/deferral_dates",
            deferralDatesRequestDto,
            Map.of("jurorNumber", String.valueOf(jurorNumber),
"locationCode", String.valueOf(locationCode)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.DeferralOptionsDto getDeferralPoolsByJurorNumberAndCourtLocationCode(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String courtLocationCode,
        java.lang.String jurorNumber) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/deferral-maintenance/available-pools/{courtLocationCode}/{jurorNumber}",
            null,
            Map.of("jurorNumber", String.valueOf(jurorNumber),
"courtLocationCode", String.valueOf(courtLocationCode)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.DeferralOptionsDto getDeferralOptionsForCourtLocation(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String courtLocationCode) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/deferral-maintenance/available-pools/{courtLocationCode}",
            null,
            Map.of("courtLocationCode", String.valueOf(courtLocationCode)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

    public java.lang.Void changeJurorsDeferralDate(JwtDetailsBureau jwtDetailsBureau,
                                                   java.lang.String jurorNumber,
                                                   uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.DeferralReasonRequestDto deferralReasonRequestDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/deferral-maintenance/deferrals/change-deferral-date/{jurorNumber}",
            deferralReasonRequestDto,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.DeferralOptionsDto getDeferralOptionsForDates(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String jurorNumber,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.DeferralDatesRequestDto deferralDatesRequestDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/deferral-maintenance/available-pools/{jurorNumber}",
            deferralDatesRequestDto,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

    public void deleteDeferral(JwtDetailsBureau jwtDetailsBureau,
                               java.lang.String jurorNumber) {
        triggerApiVoidReturn(
            HttpMethod.DELETE,
            "/api/v1/moj/deferral-maintenance/delete-deferral/{jurorNumber}",
            null,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau);
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.deferralmaintenance.DeferralResponseDto processJurorPostponement(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.deferralmaintenance.ProcessJurorPostponementRequestDto request) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/deferral-maintenance/juror/postpone",
            request,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

    public java.lang.Void moveJurorsToActivePool(JwtDetailsBureau jwtDetailsBureau,
                                                 uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.DeferralAllocateRequestDto deferralAllocateRequestDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/deferral-maintenance/deferrals/allocate-jurors-to-pool",
            deferralAllocateRequestDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

    public java.util.List<java.lang.String> getPreferredDates(JwtDetailsBureau jwtDetailsBureau,
                                                              java.lang.String jurorNumber) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/deferral-maintenance/deferral-dates/{jurorNumber}",
            null,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

}
