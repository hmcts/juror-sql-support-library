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
public class PoolRequestActiveListDto {

    @JsonProperty("poolRequestsActive")
    private java.util.List<PoolRequestActiveDataDto> data;
    @JsonProperty("totalSize")
    private long totalSize;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class PoolRequestActiveDataDto {

    @JsonProperty("courtName")
    private java.lang.String courtName;
    @JsonProperty("poolNumber")
    private java.lang.String poolNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("attendanceDate")
    private java.time.LocalDate attendanceDate;
    @JsonProperty("confirmedJurors")
    private int confirmedFromBureau;
    @JsonProperty("jurorsRequested")
    private int requestedFromBureau;
    @JsonProperty("poolCapacity")
    private int poolCapacity;
    @JsonProperty("jurorsInPool")
    private long jurorsInPool;
    @JsonProperty("poolType")
    private java.lang.String poolType;
}
}
