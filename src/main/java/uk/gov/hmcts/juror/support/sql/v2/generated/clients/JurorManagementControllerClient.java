package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.util.Map;

public class JurorManagementControllerClient extends BaseClient {

    public JurorManagementControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.jurormanagement.AttendanceDetailsResponse updateAttendance(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.jurormanagement.UpdateAttendanceDto request) {
        return triggerApi(
            HttpMethod.PATCH,
            "/api/v1/moj/juror-management/attendance",
            request,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.JurorsToDismissResponseDto retrieveJurorsToDismiss(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.JurorsToDismissRequestDto request) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/juror-management/jurors-to-dismiss",
            request,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.JurorAppearanceResponseDto.JurorAppearanceResponseData processAppearance(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.JurorAppearanceDto jurorAppearanceDto) {
        return triggerApi(
            HttpMethod.PUT,
            "/api/v1/moj/juror-management/appearance",
            jurorAppearanceDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.JurorsOnTrialResponseDto retrieveJurorsToDismiss(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String locationCode,
        java.time.LocalDate attendanceDate) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/juror-management/jurors-on-trial/{location-code}",
            null,
            Map.of("location-code", String.valueOf(locationCode)),
            Map.of("attendanceDate", String.valueOf(attendanceDate)),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.jurormanagement.AttendanceDetailsResponse retrieveAttendanceDetails(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.jurormanagement.RetrieveAttendanceDetailsDto request) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/juror-management/attendance",
            request,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.String updateAttendanceDate(JwtDetailsBureau jwtDetailsBureau,
                                                 uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.jurormanagement.UpdateAttendanceDateDto request) {
        return triggerApi(
            HttpMethod.PATCH,
            "/api/v1/moj/juror-management/attendance/attendance-date",
            request,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.JurorAppearanceResponseDto getAppearanceRecords(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String locationCode,
        java.time.LocalDate attendanceDate) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/juror-management/appearance",
            null,
            Map.of(),
            Map.of("locationCode", String.valueOf(locationCode),
                "attendanceDate", String.valueOf(attendanceDate)),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public void addAttendanceDay(JwtDetailsBureau jwtDetailsBureau,
                                 uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.AddAttendanceDayDto addAttendanceDayDto) {
        triggerApiVoidReturn(
            HttpMethod.POST,
            "/api/v1/moj/juror-management/add-attendance-day",
            addAttendanceDayDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau);
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.jurormanagement.AttendanceDetailsResponse deleteAttendance(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.jurormanagement.UpdateAttendanceDto request) {
        return triggerApi(
            HttpMethod.DELETE,
            "/api/v1/moj/juror-management/attendance",
            request,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public void confirmJuryAttendance(JwtDetailsBureau jwtDetailsBureau,
                                      uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.jurormanagement.UpdateAttendanceDto request) {
        triggerApiVoidReturn(
            HttpMethod.PATCH,
            "/api/v1/moj/juror-management/confirm-jury-attendance",
            request,
            Map.of(),
            Map.of(),
            jwtDetailsBureau);
    }

    public void addNonAttendance(JwtDetailsBureau jwtDetailsBureau,
                                 uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.jurormanagement.JurorNonAttendanceDto jurorNonAttendanceDto) {
        triggerApiVoidReturn(
            HttpMethod.POST,
            "/api/v1/moj/juror-management/non-attendance",
            jurorNonAttendanceDto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau);
    }

}
