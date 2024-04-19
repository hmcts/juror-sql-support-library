package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetails;

import java.util.Map;

public class JurorResponseControllerClient extends BaseClient {

    public JurorResponseControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    public java.lang.Object updateResponseStatus(JwtDetails jwtDetails,
java.lang.String jurorNumber,
uk.gov.hmcts.juror.support.sql.v2.generated.api.bureau.controller.request.BureauResponseStatusUpdateDto updateDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/juror-response/update-status/{jurorNumber}",
            updateDto,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {}
        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.summonsmanagement.JurorResponseRetrieveResponseDto retrieveJurorResponse(JwtDetails jwtDetails,
uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.summonsmanagement.JurorResponseRetrieveRequestDto request) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/juror-response/retrieve",
            request,
            Map.of(),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {}
        );
}

    public void updateJurorPersonalDetails(JwtDetails jwtDetails,
uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.JurorPersonalDetailsDto jurorPersonalDetailsDto,
java.lang.String jurorNumber) {
        triggerApiVoidReturn(
            HttpMethod.PATCH,
            "/api/v1/moj/juror-response/juror/{jurorNumber}/details/personal",
            jurorPersonalDetailsDto,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetails        );
}

}
