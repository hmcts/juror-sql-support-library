package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TrialListDto {

    @JsonProperty("trial_number")
    private java.lang.String trialNumber;
    @JsonProperty("parties")
    private java.lang.String defendants;
    @JsonProperty("trial_type")
    private java.lang.String trialType;
    @JsonProperty("judge")
    private java.lang.String judge;
    @JsonProperty("courtroom")
    private java.lang.String courtroom;
    @JsonProperty("court")
    private java.lang.String courtLocationName;
    @JsonProperty("court_location")
    private java.lang.String courtLocationCode;
    @JsonProperty("start_date")
    private java.time.LocalDate startDate;
    @JsonProperty("is_active")
    private java.lang.Boolean isActive;
}
