package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoliceCheckStatusDto;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetails;

import java.util.Map;

public class JurorRecordControllerClient extends BaseClient {
    public JurorRecordControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }


    public void updatePncCheckStatus(
        JwtDetails jwtDetails,
        String jurorNumber,
        PoliceCheckStatusDto request
    ) {
        triggerApiVoidReturn(
            HttpMethod.PATCH,
            "/api/v1/moj/juror-record/pnc/{jurorNumber}",
            request,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetails
        );

    }
}
