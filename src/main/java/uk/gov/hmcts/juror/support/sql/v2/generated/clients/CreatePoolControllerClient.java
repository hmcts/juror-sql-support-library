package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetails;

import java.util.Map;

public class CreatePoolControllerClient extends BaseClient {

    public CreatePoolControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.PaginatedList<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.FilterPoolMember> getPoolMembers(
        JwtDetails jwtDetails,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolMemberFilterRequestQuery query) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/pool-create/members",
            query,
            Map.of(),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.String convertNilPool(JwtDetails jwtDetails,
                                           uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolRequestDto poolRequestDto) {
        return triggerApi(
            HttpMethod.PUT,
            "/api/v1/moj/pool-create/nil-pool-convert",
            poolRequestDto,
            Map.of(),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.SummonsFormResponseDto getPoolRequest(
        JwtDetails jwtDetails,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.SummonsFormRequestDto summonsFormRequestDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/pool-create/summons-form",
            summonsFormRequestDto,
            Map.of(),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.CoronerPoolItemDto getCoronerPoolRequest(
        JwtDetails jwtDetails,
        java.lang.String poolNumber) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-create/coroner-pool",
            null,
            Map.of(),
            Map.of("poolNumber", String.valueOf(poolNumber)),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.String createNilPool(JwtDetails jwtDetails,
                                          uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.NilPoolRequestDto nilPoolRequestDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/pool-create/nil-pool-create",
            nilPoolRequestDto,
            Map.of(),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.NilPoolResponseDto checkNilPoolRequest(
        JwtDetails jwtDetails,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.NilPoolRequestDto nilPoolRequestDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/pool-create/nil-pool-check",
            nilPoolRequestDto,
            Map.of(),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.String createPoolRequest(JwtDetails jwtDetails,
                                              uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolCreateRequestDto poolCreateRequestDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/pool-create/create-pool",
            poolCreateRequestDto,
            Map.of(),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.util.List<java.lang.String> getThinPoolMembers(JwtDetails jwtDetails,
                                                               java.lang.String poolNumber) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-create/members/{poolNumber}",
            null,
            Map.of("poolNumber", String.valueOf(poolNumber)),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.Long getBureauDeferrals(JwtDetails jwtDetails,
                                             java.lang.String locationCode,
                                             java.time.LocalDate deferredTo) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-create/bureau-deferrals",
            null,
            Map.of(),
            Map.of("deferredTo", String.valueOf(deferredTo),
                "locationCode", String.valueOf(locationCode)),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.PostcodesListDto getCourtCatchmentItems(
        JwtDetails jwtDetails,
        java.lang.String areaCode,
        boolean isCoronersPool) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-create/postcodes",
            null,
            Map.of(),
            Map.of("areaCode", String.valueOf(areaCode),
                "isCoronersPool", String.valueOf(isCoronersPool)),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.String additionalSummonsForPool(JwtDetails jwtDetails,
                                                     uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolAdditionalSummonsDto poolAdditionalSummonsDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/pool-create/additional-summons",
            poolAdditionalSummonsDto,
            Map.of(),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.PoolRequestItemDto getPoolRequest(
        JwtDetails jwtDetails,
        java.lang.String poolNumber,
        java.lang.String owner) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-create/pool",
            null,
            Map.of(),
            Map.of("owner", String.valueOf(owner),
                "poolNumber", String.valueOf(poolNumber)),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.Object createCoronerPool(JwtDetails jwtDetails,
                                              uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.CoronerPoolRequestDto coronerPoolRequestDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/pool-create/create-coroner-pool",
            coronerPoolRequestDto,
            Map.of(),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.String addCitizensToCoronerPool(JwtDetails jwtDetails,
                                                     uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.CoronerPoolAddCitizenRequestDto coronerPoolAddCitizenRequestDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/pool-create/add-citizens",
            coronerPoolAddCitizenRequestDto,
            Map.of(),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

}
