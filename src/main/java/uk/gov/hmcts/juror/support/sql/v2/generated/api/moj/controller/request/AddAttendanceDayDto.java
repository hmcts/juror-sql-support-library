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
public class AddAttendanceDayDto {

    @JsonProperty("juror_number")
    private java.lang.String jurorNumber;
    @JsonProperty("pool_number")
    private java.lang.String poolNumber;
    @JsonProperty("location_code")
    private java.lang.String locationCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("attendance_date")
    private java.time.LocalDate attendanceDate;
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("check_in_time")
    private java.time.LocalTime checkInTime;
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("check_out_time")
    private java.time.LocalTime checkOutTime;
}
