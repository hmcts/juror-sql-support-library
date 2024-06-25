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
public class PoolsAtCourtLocationListDto {

    @JsonProperty("pools_at_court_location")
    private java.util.List<PoolsAtCourtLocationDataDto> data;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class PoolsAtCourtLocationDataDto {

    @JsonProperty("pool_number")
    private java.lang.String poolNumber;
    @JsonProperty("jurors_on_trials")
    private int jurorsOnTrials;
    @JsonProperty("jurors_in_attendance")
    private int jurorsInAttendance;
    @JsonProperty("jurors_on_call")
    private int jurorsOnCall;
    @JsonProperty("other_jurors")
    private int otherJurors;
    @JsonProperty("total_jurors")
    private int totalJurors;
    @JsonProperty("pool_type")
    private java.lang.String poolType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("service_start_date")
    private java.time.LocalDate serviceStartDate;
}
}
