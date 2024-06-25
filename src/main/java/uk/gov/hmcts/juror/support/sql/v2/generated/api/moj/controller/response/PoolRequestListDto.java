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
public class PoolRequestListDto {

    @JsonProperty("poolRequests")
    private java.util.List<PoolRequestDataDto> data;
    @JsonProperty("totalSize")
    private long totalSize;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class PoolRequestDataDto {

    @JsonProperty("courtName")
    private java.lang.String courtName;
    @JsonProperty("poolNumber")
    private java.lang.String poolNumber;
    @JsonProperty("poolType")
    private java.lang.String poolType;
    @JsonProperty("numberRequested")
    private int numberRequested;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("attendanceDate")
    private java.time.LocalDate attendanceDate;
}
}
