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
public class JurorsOnTrialResponseDto {

    @JsonProperty("trials_list")
    private java.util.List<JurorsOnTrialResponseData> trialsList;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class JurorsOnTrialResponseData {

    @JsonProperty("trial_number")
    private java.lang.String trialNumber;
    @JsonProperty("parties")
    private java.lang.String parties;
    @JsonProperty("trial_type")
    private java.lang.String trialType;
    @JsonProperty("judge")
    private java.lang.String judge;
    @JsonProperty("courtroom")
    private java.lang.String courtroom;
    @JsonProperty("jurors_attended")
    private long numberAttended;
    @JsonProperty("total_jurors")
    private long totalJurors;
    @JsonProperty("attendance_audit")
    private java.lang.String attendanceAudit;
}
}
