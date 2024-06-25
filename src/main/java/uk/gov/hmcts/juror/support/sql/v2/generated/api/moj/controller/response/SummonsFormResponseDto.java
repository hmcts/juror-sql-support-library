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
public class SummonsFormResponseDto {

    @JsonProperty("bureauDeferrals")
    private java.lang.Integer bureauDeferrals;
    @JsonProperty("numberRequired")
    private java.lang.Integer numberRequired;
    @JsonProperty("courtCatchmentItems")
    private java.util.List<CourtCatchmentSummaryItem> courtCatchmentSummaryItems;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class CourtCatchmentSummaryItem {

    private java.lang.String postCodePart;
    private java.lang.Integer total;
}
}
