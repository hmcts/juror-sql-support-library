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
public class ReasonableAdjustmentDetailsDto {

    @JsonProperty("specialNeeds")
    private java.util.List<ReasonableAdjustment> reasonableAdjustments;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class ReasonableAdjustment {

    @JsonProperty("assistanceType")
    private java.lang.String assistanceType;
    @JsonProperty("assistanceTypeDetails")
    private java.lang.String assistanceTypeDetails;
}
}
