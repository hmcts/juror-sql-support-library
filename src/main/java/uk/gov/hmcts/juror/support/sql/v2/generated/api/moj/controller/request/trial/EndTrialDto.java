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
public class EndTrialDto {

    @JsonProperty("trial_end_date")
    private java.time.LocalDate trialEndDate;
    @JsonProperty("trial_number")
    private java.lang.String trialNumber;
    @JsonProperty("location_code")
    private java.lang.String locationCode;
}
