package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpServerErrorException;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorPoolRepository;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
public class CreatePoolControllerClient extends BaseClient {

    public CreatePoolControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    public java.lang.String additionalSummonsForPool(JwtDetailsBureau jwtDetailsBureau,
                                                     uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolAdditionalSummonsDto poolAdditionalSummonsDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/pool-create/additional-summons",
            poolAdditionalSummonsDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.String addCitizensToCoronerPool(JwtDetailsBureau jwtDetailsBureau,
                                                     uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.CoronerPoolAddCitizenRequestDto coronerPoolAddCitizenRequestDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/pool-create/add-citizens",
            coronerPoolAddCitizenRequestDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.Object createCoronerPool(JwtDetailsBureau jwtDetailsBureau,
                                              uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.CoronerPoolRequestDto coronerPoolRequestDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/pool-create/create-coroner-pool",
            coronerPoolRequestDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.PoolRequestItemDto getPoolRequest(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String poolNumber,
        java.lang.String owner) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-create/pool",
            null,
            Map.of(),
            Map.of("owner", String.valueOf(owner),
                "poolNumber", String.valueOf(poolNumber)),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.PostcodesListDto getCourtCatchmentItems(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String areaCode,
        boolean isCoronersPool) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-create/postcodes",
            null,
            Map.of(),
            Map.of("areaCode", String.valueOf(areaCode),
                "isCoronersPool", String.valueOf(isCoronersPool)),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.util.List<java.lang.String> getThinPoolMembers(JwtDetailsBureau jwtDetailsBureau,
                                                               java.lang.String poolNumber) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-create/members/{poolNumber}",
            null,
            Map.of("poolNumber", String.valueOf(poolNumber)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.NilPoolResponseDto checkNilPoolRequest(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.NilPoolRequestDto nilPoolRequestDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/pool-create/nil-pool-check",
            nilPoolRequestDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.SummonsFormResponseDto getPoolRequest(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.SummonsFormRequestDto summonsFormRequestDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/pool-create/summons-form",
            summonsFormRequestDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.String createPoolRequest(
        JurorPoolRepository jurorPoolRepository,
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolCreateRequestDto poolCreateRequestDto) {
        try {
            return triggerApi(
                HttpMethod.POST,
                "/api/v1/moj/pool-create/create-pool",
                poolCreateRequestDto,
                Map.of(),
                Map.of(),
                jwtDetailsBureau,
                new ParameterizedTypeReference<>() {
                }
            );
        } catch (Exception e) {
            if (e instanceof HttpServerErrorException.GatewayTimeout || e.getMessage().equals("timeout")) {
                log.info("Timeout occurred, waiting for pool to be created");
                LocalDateTime maxTime = LocalDateTime.now().plusMinutes(10);
                while (LocalDateTime.now().isBefore(maxTime)) {
                    if (jurorPoolRepository.findAllByPoolNumber(poolCreateRequestDto.getPoolNumber()).size()
                        == poolCreateRequestDto.getNoRequested()) {
                        return "success";
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            throw e;
        }
    }

    public java.lang.String convertNilPool(JwtDetailsBureau jwtDetailsBureau,
                                           uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolRequestDto poolRequestDto) {
        return triggerApi(
            HttpMethod.PUT,
            "/api/v1/moj/pool-create/nil-pool-convert",
            poolRequestDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.Long getBureauDeferrals(JwtDetailsBureau jwtDetailsBureau,
                                             java.lang.String locationCode,
                                             java.time.LocalDate deferredTo) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-create/bureau-deferrals",
            null,
            Map.of(),
            Map.of("deferredTo", String.valueOf(deferredTo),
                "locationCode", String.valueOf(locationCode)),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.CoronerPoolItemDto
    getCoronerPoolRequest(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String poolNumber) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/pool-create/coroner-pool",
            null,
            Map.of(),
            Map.of("poolNumber", String.valueOf(poolNumber)),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.PaginatedList<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.FilterPoolMember>
    getPoolMembers(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolMemberFilterRequestQuery query) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/pool-create/members",
            query,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.String createNilPool(JwtDetailsBureau jwtDetailsBureau,
                                          uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.NilPoolRequestDto nilPoolRequestDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/pool-create/nil-pool-create",
            nilPoolRequestDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

}
