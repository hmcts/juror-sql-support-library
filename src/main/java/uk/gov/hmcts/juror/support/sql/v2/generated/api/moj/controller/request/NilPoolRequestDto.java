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
public class NilPoolRequestDto {

    @JsonProperty("courtCode")
    private java.lang.String locationCode;
    @JsonProperty("locationName")
    private java.lang.String locationName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("attendanceDate")
    private java.time.LocalDate attendanceDate;
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("attendanceTime")
    private java.time.LocalTime attendanceTime;
    @JsonProperty("poolType")
    private java.lang.String poolType;
    @JsonProperty("poolNumber")
    private java.lang.String poolNumber;
}
