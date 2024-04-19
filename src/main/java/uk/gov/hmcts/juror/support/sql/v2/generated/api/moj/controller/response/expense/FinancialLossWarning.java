package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense;

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
public class FinancialLossWarning {

    @JsonProperty("date")
    private java.time.LocalDate date;
    @JsonProperty("juror_loss")
    private java.math.BigDecimal jurorsLoss;
    @JsonProperty("limit")
    private java.math.BigDecimal limit;
    @JsonProperty("attendance_type")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.PayAttendanceType attendanceType;
    @JsonProperty("is_long_trial_day")
    private java.lang.Boolean isLongTrialDay;
    @JsonProperty("message")
    private java.lang.String message;
}
