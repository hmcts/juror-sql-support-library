package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.jurormanagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AttendanceDetailsResponse {

    @JsonProperty("details")
    private java.util.List<Details> details;
    @JsonProperty("summary")
    private Summary summary;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Details {

        @JsonProperty("juror_number")
        private java.lang.String jurorNumber;
        @JsonProperty("first_name")
        private java.lang.String firstName;
        @JsonProperty("last_name")
        private java.lang.String lastName;
        @JsonProperty("juror_status")
        private java.lang.Integer jurorStatus;
        @JsonProperty("check_in_time")
        private java.time.LocalTime checkInTime;
        @JsonProperty("check_out_time")
        private java.time.LocalTime checkOutTime;
        @JsonProperty("isNoShow")
        private java.lang.Boolean isNoShow;
        @JsonProperty("appStage")
        private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.AppearanceStage appearanceStage;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Summary {

        @JsonProperty("checkedIn")
        private long checkedIn;
        @JsonProperty("checkedOut")
        private long checkedOut;
        @JsonProperty("checkedInAndOut")
        private long checkedInAndOut;
        @JsonProperty("panelled")
        private long panelled;
        @JsonProperty("absent")
        private long absent;
        @JsonProperty("deleted")
        private long deleted;
        @JsonProperty("additionalInformation")
        private java.lang.String additionalInformation;
    }
}