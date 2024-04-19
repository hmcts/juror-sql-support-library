package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.util.Map;

public class ManagePoolControllerClient extends BaseClient {

    public ManagePoolControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.PoolSummaryResponseDto getPoolStatistics(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String poolNumber) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/manage-pool/summary",
            null,
            Map.of(),
            Map.of("poolNumber", String.valueOf(poolNumber)),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.JurorManagementResponseDto validatePoolMembers(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.JurorManagementRequestDto requestDto) {
        return triggerApi(
            HttpMethod.PUT,
            "/api/v1/moj/manage-pool/movement/validation",
            requestDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

    public java.lang.Integer transferJurorsToNewCourt(JwtDetailsBureau jwtDetailsBureau,
                                                      uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.JurorManagementRequestDto requestDto) {
        return triggerApi(
            HttpMethod.PUT,
            "/api/v1/moj/manage-pool/transfer",
            requestDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.SummoningProgressResponseDto getPoolMonitoringStats(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String courtLocationCode,
        java.lang.String poolType) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/manage-pool/summoning-progress/{courtLocCode}/{poolType}",
            null,
            Map.of("courtLocCode", String.valueOf(courtLocationCode),
"poolType", String.valueOf(poolType)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.poolmanagement.AvailablePoolsInCourtLocationDto getAvailablePoolsInCourtLocation(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String locCode) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/manage-pool/available-pools/{locCode}",
            null,
            Map.of("locCode", String.valueOf(locCode)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

    public java.lang.Object deletePool(JwtDetailsBureau jwtDetailsBureau,
                                       java.lang.String poolNumber) {
        return triggerApi(
            HttpMethod.DELETE,
            "/api/v1/moj/manage-pool/delete",
            null,
            Map.of(),
            Map.of("poolNumber", String.valueOf(poolNumber)),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

    public java.lang.Object editPool(JwtDetailsBureau jwtDetailsBureau,
                                     uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolEditRequestDto poolEditRequestDto) {
        return triggerApi(
            HttpMethod.PUT,
            "/api/v1/moj/manage-pool/edit-pool",
            poolEditRequestDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

    public java.lang.Integer reassignJurors(JwtDetailsBureau jwtDetailsBureau,
                                            uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.JurorManagementRequestDto jurorManagementRequestDto) {
        return triggerApi(
            HttpMethod.PUT,
            "/api/v1/moj/manage-pool/reassign-jurors",
            jurorManagementRequestDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.poolmanagement.AvailablePoolsInCourtLocationDto getAvailablePoolsInCourtLocationCourtOwned(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String locCode) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/manage-pool/available-pools-court-owned/{locCode}",
            null,
            Map.of("locCode", String.valueOf(locCode)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {}
        );
}

}
