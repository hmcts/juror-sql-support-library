package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetails;

import java.util.Map;

public class CompleteServiceControllerClient extends BaseClient {

    public CompleteServiceControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    public void completeService(JwtDetails jwtDetails,
java.lang.String poolNumber,
uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.CompleteServiceJurorNumberListDto completeServiceJurorNumberListDto) {
        triggerApiVoidReturn(
            HttpMethod.PATCH,
            "/api/v1/moj/complete-service/{poolNumber}/complete",
            completeServiceJurorNumberListDto,
            Map.of("poolNumber", String.valueOf(poolNumber)),
            Map.of(),
            jwtDetails        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.PaginatedList<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.CompleteJurorResponse> getCompleteJurors(JwtDetails jwtDetails,
uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.JurorPoolSearch request) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/complete-service",
            request,
            Map.of(),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {}
        );
}

    public void completeDismissedJurorsService(JwtDetails jwtDetails,
uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.CompleteServiceJurorNumberListDto completeServiceJurorNumberListDto) {
        triggerApiVoidReturn(
            HttpMethod.PATCH,
            "/api/v1/moj/complete-service/dismissal",
            completeServiceJurorNumberListDto,
            Map.of(),
            Map.of(),
            jwtDetails        );
}

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.CompleteServiceValidationResponseDto validateCompleteService(JwtDetails jwtDetails,
java.lang.String poolNumber,
uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.JurorNumberListDto jurorNumberListDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/complete-service/{poolNumber}/validate",
            jurorNumberListDto,
            Map.of("poolNumber", String.valueOf(poolNumber)),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {}
        );
}

    public void uncompleteService(JwtDetails jwtDetails,
java.util.List requestList) {
        triggerApiVoidReturn(
            HttpMethod.PATCH,
            "/api/v1/moj/complete-service/uncomplete",
            requestList,
            Map.of(),
            Map.of(),
            jwtDetails        );
}

}
