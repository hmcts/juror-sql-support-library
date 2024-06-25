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
public class CreatePanelDto {

    @JsonProperty("trial_number")
    private java.lang.String trialNumber;
    @JsonProperty("number_requested")
    private int numberRequested;
    @JsonProperty("pool_numbers")
    private java.util.List<java.lang.String> poolNumbers;
    @JsonProperty("court_location_code")
    private java.lang.String courtLocationCode;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("attendance_date")
    private LocalDate attendanceDate;
}
