package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.util.Map;

public class AdministrationCourtRoomControllerClient extends BaseClient {

    public AdministrationCourtRoomControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    public java.lang.Void updateCourtRoom(JwtDetailsBureau jwtDetailsBureau,
                                          java.lang.String locCode,
                                          java.lang.Long id,
                                          uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.administration.CourtRoomDto courtRoomDto) {
        return triggerApi(
            HttpMethod.PUT,
            "/api/v1/moj/administration/court-rooms/{loc_code}/{id}",
            courtRoomDto,
            Map.of("loc_code", String.valueOf(locCode),
                "id", String.valueOf(id)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.Void createCourtRoom(JwtDetailsBureau jwtDetailsBureau,
                                          java.lang.String locCode,
                                          uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.administration.CourtRoomDto courtRoomDto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/administration/court-rooms/{loc_code}",
            courtRoomDto,
            Map.of("loc_code", String.valueOf(locCode)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.administration.CourtRoomWithIdDto> viewCourtRoomsDetails(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String locCode) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/administration/court-rooms/{loc_code}",
            null,
            Map.of("loc_code", String.valueOf(locCode)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.administration.CourtRoomWithIdDto viewCourtRoomDetails(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String locCode,
        java.lang.Long id) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/administration/court-rooms/{loc_code}/{id}",
            null,
            Map.of("loc_code", String.valueOf(locCode),
                "id", String.valueOf(id)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

}
