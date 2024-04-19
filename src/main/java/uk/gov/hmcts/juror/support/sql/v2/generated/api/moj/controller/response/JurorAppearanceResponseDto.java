package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JurorAppearanceResponseDto {

    @JsonProperty("juror_appearance_response_data")
    private java.util.List<JurorAppearanceResponseData> data;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class JurorAppearanceResponseData {

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
        @JsonProperty("noShow")
        private java.lang.Boolean noShow;
        @JsonProperty("appStage")
        private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.AppearanceStage appStage;
    }
}
