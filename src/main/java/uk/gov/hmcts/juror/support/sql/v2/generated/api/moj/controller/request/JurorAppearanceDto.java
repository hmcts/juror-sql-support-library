package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JurorAppearanceDto {

    @JsonProperty("juror_number")
    private java.lang.String jurorNumber;
    @JsonProperty("location_code")
    private java.lang.String locationCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("attendance_date")
    private java.time.LocalDate attendanceDate;
    @JsonProperty("check_in_time")
    private java.time.LocalTime checkInTime;
    @JsonProperty("check_out_time")
    private java.time.LocalTime checkOutTime;
    @JsonProperty("appearance_stage")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.AppearanceStage appearanceStage;
}
