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
public class ReturnJuryDto {

    @JsonProperty("check_in")
    private java.lang.String checkIn;
    @JsonProperty("check_out")
    private java.lang.String checkOut;
    @JsonProperty("completed")
    private java.lang.Boolean completed;
    @JsonProperty("jurors")
    private java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.JurorDetailRequestDto> jurors;

    @JsonProperty("attendance_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate attendanceDate;
}
