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
public class PoolNumbersListDto {

    @JsonProperty("poolNumbers")
    private java.util.List<PoolNumbersDataDto> data;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class PoolNumbersDataDto {

    @JsonProperty("poolNumber")
    private java.lang.String poolNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("attendanceDate")
    private java.time.LocalDate attendanceDate;
}
}
