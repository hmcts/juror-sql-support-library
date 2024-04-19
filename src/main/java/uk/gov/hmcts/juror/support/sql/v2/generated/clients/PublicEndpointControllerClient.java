package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetails;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.util.Map;

public class PublicEndpointControllerClient extends BaseClient {

    public PublicEndpointControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    @SneakyThrows
    public java.lang.String respondToSummons(JwtDetails jwtDetailsBureau,
                                             uk.gov.hmcts.juror.support.sql.v2.generated.api.juror.controller.request.JurorResponseDto responseDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/public/juror/respond",
            responseDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.juror.controller.response.JurorHolidaysResponseDto jurorHolidayDates(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.config.public1.PublicJwtPayload principal,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.juror.controller.request.JurorHolidaysRequestDto jurorHolidaysRequestDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/public/deferral-dates",
            jurorHolidaysRequestDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.juror.controller.response.JurorDetailDto retrieveJurorById(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.config.public1.PublicJwtPayload principal,
        java.lang.String jurorNumber) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/public/juror/{jurorNumber}",
            null,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

}
