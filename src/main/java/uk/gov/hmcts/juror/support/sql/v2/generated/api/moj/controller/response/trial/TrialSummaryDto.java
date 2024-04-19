package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class TrialSummaryDto {

    @JsonProperty("trial_number")
    private java.lang.String trialNumber;
    @JsonProperty("")
    private java.lang.String defendants;
    @JsonProperty("trial_type")
    private java.lang.String trialType;
    @JsonProperty("judge")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial.JudgeDto judge;
    @JsonProperty("courtroom")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial.CourtroomsDto courtroomsDto;
    @JsonProperty("start_date")
    private java.time.LocalDate trialStartDate;
    @JsonProperty("protected")
    private java.lang.Boolean protectedTrial;
    @JsonProperty("is_active")
    private java.lang.Boolean isActive;
    @JsonProperty("is_jury_empanelled")
    private java.lang.Boolean isJuryEmpanelled;
    @JsonProperty("trial_end_date")
    private java.time.LocalDate trialEndDate;
}
