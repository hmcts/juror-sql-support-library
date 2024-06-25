package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.util.Map;

public class ExcusalResponseControllerClient extends BaseClient {

    public ExcusalResponseControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    public void respondToExcusalRequest(JwtDetailsBureau jwtDetailsBureau,
                                        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.ExcusalDecisionDto excusalDecisionDto,
                                        java.lang.String jurorNumber) {
        triggerApiVoidReturn(
            HttpMethod.PUT,
            "/api/v1/moj/excusal-response/juror/{jurorNumber}",
            excusalDecisionDto,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau);
}

}
