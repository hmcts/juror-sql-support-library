package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.util.Map;

public class DisqualifyJurorControllerClient extends BaseClient {

    public DisqualifyJurorControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    public java.lang.String disqualifyJuror(JwtDetailsBureau jwtDetailsBureau,
                                            java.lang.String jurorNumber,
                                            uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.summonsmanagement.DisqualifyJurorDto disqualifyJuror) {
        return triggerApi(
            HttpMethod.PATCH,
            "/api/v1/moj/disqualify/juror/{jurorNumber}",
            disqualifyJuror,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.String disqualifyJurorDueToAge(JwtDetailsBureau jwtDetailsBureau,
                                                    uk.gov.hmcts.juror.support.sql.v2.generated.api.config.bureau.BureauJwtAuthentication bureauJwtAuthentication,
                                                    java.lang.String jurorNumber) {
        return triggerApi(
            HttpMethod.PATCH,
            "/api/v1/moj/disqualify/juror/{jurorNumber}/age",
            null,
            Map.of("jurorNumber", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.summonsmanagement.DisqualifyReasonsDto disqualifyReasons(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.config.bureau.BureauJwtAuthentication auth) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/disqualify/reasons",
            null,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

}
