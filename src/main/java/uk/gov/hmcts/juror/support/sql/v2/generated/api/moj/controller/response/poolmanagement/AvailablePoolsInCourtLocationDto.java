package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.poolmanagement;

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
public class AvailablePoolsInCourtLocationDto {

    @JsonProperty("availablePools")
    private java.util.List<AvailablePoolsDto> availablePools;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class AvailablePoolsDto {

        @JsonProperty("poolNumber")
        private java.lang.String poolNumber;
        @JsonFormat(pattern = "yyyy-MM-dd")
        @JsonProperty("serviceStartDate")
        private java.time.LocalDate serviceStartDate;
        @JsonProperty("utilisation")
        private long utilisation;
    }
}
