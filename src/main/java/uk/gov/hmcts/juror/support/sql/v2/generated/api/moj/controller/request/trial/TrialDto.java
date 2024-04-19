package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial;

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
public class TrialDto {

    @JsonProperty("case_number")
    private java.lang.String caseNumber;
    @JsonProperty("trial_type")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.trial.TrialType trialType;
    @JsonProperty("defendant")
    private java.lang.String defendant;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("start_date")
    private java.time.LocalDate startDate;
    @JsonProperty("judge_id")
    private java.lang.Long judgeId;
    @JsonProperty("court_location")
    private java.lang.String courtLocation;
    @JsonProperty("courtroom_id")
    private java.lang.Long courtroomId;
    @JsonProperty("protected_trial")
    private boolean protectedTrial;
}
