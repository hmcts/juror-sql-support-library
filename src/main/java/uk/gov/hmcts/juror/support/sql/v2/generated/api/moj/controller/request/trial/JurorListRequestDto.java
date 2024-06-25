package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JurorListRequestDto {

    @JsonProperty("jurors")
    private java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.JurorDetailRequestDto> jurors;
    @JsonProperty("trial_number")
    private java.lang.String trialNumber;
    @JsonProperty("court_location_code")
    private java.lang.String courtLocationCode;
    @JsonProperty("number_requested")
    private int numberRequested;

    @JsonProperty("attendance_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate attendanceDate;
}
