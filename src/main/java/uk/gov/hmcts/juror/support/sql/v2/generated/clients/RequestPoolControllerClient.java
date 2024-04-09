package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetails;

import java.util.Map;

public class RequestPoolControllerClient extends BaseClient {

    public RequestPoolControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    public java.lang.Long getCourtDeferrals(JwtDetails jwtDetails,
java.lang.String locationCode,
java.time.LocalDate deferredTo) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-request/deferrals",
            null,
            Map.of(),
            Map.of("deferredTo", String.valueOf(deferredTo),
"locationCode", String.valueOf(locationCode)),
            jwtDetails,
            new ParameterizedTypeReference<>() {}
        );
}

    public void createPoolRequest(JwtDetails jwtDetails,
uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolRequestDto poolRequestDto) {
        triggerApiVoidReturn(
            HttpMethod.POST,
            "/api/v1/moj/pool-request/new-pool",
            poolRequestDto,
            Map.of(),
            Map.of(),
            jwtDetails        );
}

    public java.lang.String generatePoolNumber(JwtDetails jwtDetails,
java.lang.String locationCode,
java.time.LocalDate attendanceDate) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-request/generate-pool-number",
            null,
            Map.of(),
            Map.of("locationCode", String.valueOf(locationCode),
"attendanceDate", String.valueOf(attendanceDate)),
            jwtDetails,
            new ParameterizedTypeReference<>() {}
        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.PoolNumbersListDto getPoolNumbers(JwtDetails jwtDetails,
java.lang.String poolNumberPrefix) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-request/pool-numbers",
            null,
            Map.of(),
            Map.of("poolNumberPrefix", String.valueOf(poolNumberPrefix)),
            jwtDetails,
            new ParameterizedTypeReference<>() {}
        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.DayType getDayType(JwtDetails jwtDetails,
java.lang.String locationCode,
java.time.LocalDate attendanceDate) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-request/day-type",
            null,
            Map.of(),
            Map.of("locationCode", String.valueOf(locationCode),
"attendanceDate", String.valueOf(attendanceDate)),
            jwtDetails,
            new ParameterizedTypeReference<>() {}
        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.CourtLocationListDto getCourtLocations(JwtDetails jwtDetails) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-request/court-locations",
            null,
            Map.of(),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {}
        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.PoolRequestActiveListDto getActivePoolRequests(JwtDetails jwtDetails,
java.lang.String locCode,
java.lang.String tab,
int offset,
java.lang.String sortBy,
java.lang.String sortOrder) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-request/pools-active",
            null,
            Map.of(),
            Map.of("locCode", String.valueOf(locCode),
"tab", String.valueOf(tab),
"offset", String.valueOf(offset),
"sortOrder", String.valueOf(sortOrder),
"sortBy", String.valueOf(sortBy)),
            jwtDetails,
            new ParameterizedTypeReference<>() {}
        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.PoolRequestListDto getPoolRequests(JwtDetails jwtDetails,
java.lang.String locCode,
int offset,
java.lang.String sortBy,
java.lang.String sortOrder) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-request/pools-requested",
            null,
            Map.of(),
            Map.of("locCode", String.valueOf(locCode),
"offset", String.valueOf(offset),
"sortOrder", String.valueOf(sortOrder),
"sortBy", String.valueOf(sortBy)),
            jwtDetails,
            new ParameterizedTypeReference<>() {}
        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.PoolsAtCourtLocationListDto getActivePoolsAtCourt(JwtDetails jwtDetails,
java.lang.String locCode) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-request/pools-at-court",
            null,
            Map.of(),
            Map.of("locCode", String.valueOf(locCode)),
            jwtDetails,
            new ParameterizedTypeReference<>() {}
        );
}

}
