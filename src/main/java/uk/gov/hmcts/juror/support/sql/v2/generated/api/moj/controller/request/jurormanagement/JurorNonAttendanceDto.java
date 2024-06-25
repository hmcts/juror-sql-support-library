package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.jurormanagement;

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
public class JurorNonAttendanceDto {

    @JsonProperty("juror_number")
    private java.lang.String jurorNumber;
    @JsonProperty("location_code")
    private java.lang.String locationCode;
    @JsonProperty("pool_number")
    private java.lang.String poolNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("non_attendance_date")
    private java.time.LocalDate nonAttendanceDate;
}
