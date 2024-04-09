package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response;

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
public class CourtLocationDataDto {

    @JsonProperty("locationCode")
    private java.lang.String locationCode;
    @JsonProperty("locationName")
    private java.lang.String locationName;
    @JsonProperty("attendanceTime")
    private java.lang.String attendanceTime;
    @JsonProperty("owner")
    private java.lang.String owner;
}
