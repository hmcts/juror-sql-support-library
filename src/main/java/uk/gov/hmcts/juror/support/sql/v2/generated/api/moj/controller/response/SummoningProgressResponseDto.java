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
public class SummoningProgressResponseDto {

    @JsonProperty("statsByWeek")
    private java.util.List<WeekFilter> statsByWeek;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class WeekFilter {

    @JsonProperty("stats")
    private java.util.List<SummoningProgressStats> stats;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("startOfWeek")
    private java.time.LocalDate startOfWeek;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class SummoningProgressStats {

    @JsonProperty("poolNumber")
    private java.lang.String poolNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("serviceStartDate")
    private java.time.LocalDate serviceStartDate;
    @JsonProperty("requested")
    private int requested;
    @JsonProperty("summoned")
    private int summoned;
    @JsonProperty("confirmed")
    private int confirmed;
    @JsonProperty("balance")
    private int balance;
    @JsonProperty("")
    private int unavailable;
}
}
}
