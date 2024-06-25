package uk.gov.hmcts.juror.support.sql.v2.generated.clients.trial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetails;

import java.util.Map;

public class PanelControllerClient extends BaseClient {

    public PanelControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    public java.lang.Boolean getPanelCreationStatus(JwtDetails jwtDetails,
                                                    java.lang.String trialNumber,
                                                    java.lang.String courtLocationCode) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/trial/panel/status",
            null,
            Map.of("trialNumber", String.valueOf(trialNumber),
                "courtLocationCode", String.valueOf(courtLocationCode)),
            Map.of("trial_number", String.valueOf(trialNumber),
                "court_location_code", String.valueOf(courtLocationCode)),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial.AvailableJurorsDto> getAvailableJurors(
        JwtDetails jwtDetails,
        java.lang.String locationCode) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/trial/panel/available-jurors",
            null,
            Map.of("locationCode", String.valueOf(locationCode)),
            Map.of("court_location_code", String.valueOf(locationCode)),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial.PanelListDto> processEmpanelled(
        JwtDetails jwtDetails,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.JurorListRequestDto jurorListRequestDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/trial/panel/process-empanelled",
            jurorListRequestDto,
            Map.of(),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial.PanelListDto> createPanel(
        JwtDetails jwtDetails,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.CreatePanelDto createPanelDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/trial/panel/create-panel",
            createPanelDto,
            Map.of(),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial.EmpanelListDto requestEmpanel(
        JwtDetails jwtDetails,
        java.lang.String trialNumber,
        int numberRequested,
        java.lang.String courtLocationCode) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/trial/panel/request-empanel",
            null,
            Map.of("trialNumber", String.valueOf(trialNumber),
                "numberRequested", String.valueOf(numberRequested),
                "courtLocationCode", String.valueOf(courtLocationCode)),
            Map.of("trial_number", String.valueOf(trialNumber),
                "court_location_code", String.valueOf(courtLocationCode),
                "number_requested", String.valueOf(numberRequested)),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial.PanelListDto> addPanelMembers(
        JwtDetails jwtDetails,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.CreatePanelDto createPanelDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/trial/panel/add-panel-members",
            createPanelDto,
            Map.of(),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial.PanelListDto> getPanel(
        JwtDetails jwtDetails,
        java.lang.String trialNumber,
        java.lang.String courtLocationCode) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/trial/panel/list",
            null,
            Map.of("trialNumber", String.valueOf(trialNumber),
                "courtLocationCode", String.valueOf(courtLocationCode)),
            Map.of("trial_number", String.valueOf(trialNumber),
                "court_location_code", String.valueOf(courtLocationCode)),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

}
