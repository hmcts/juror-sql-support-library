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
public class EligibilityDetailsDto {

    @JsonProperty("eligibility")
    private Eligibility eligibility;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class Eligibility {

    @JsonProperty("livedConsecutive")
    private java.lang.Boolean livedConsecutive;
    @JsonProperty("mentalHealthAct")
    private java.lang.Boolean mentalHealthAct;
    @JsonProperty("mentalHealthCapacity")
    private java.lang.Boolean mentalHealthCapacity;
    @JsonProperty("onBail")
    private java.lang.Boolean onBail;
    @JsonProperty("convicted")
    private java.lang.Boolean convicted;
}
}
