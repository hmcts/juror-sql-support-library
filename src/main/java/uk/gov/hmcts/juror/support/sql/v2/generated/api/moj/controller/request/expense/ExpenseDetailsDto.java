package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense;

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
public class ExpenseDetailsDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("attendance_date")
    private java.time.LocalDate attendanceDate;
    @JsonProperty("attendance_type")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.AttendanceType attendanceType;
    @JsonProperty("payment_method")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.PaymentMethod paymentMethod;
}
