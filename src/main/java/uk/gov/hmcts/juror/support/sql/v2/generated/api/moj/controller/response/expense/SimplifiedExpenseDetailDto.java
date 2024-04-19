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
public class SimplifiedExpenseDetailDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.time.LocalDate attendanceDate;
    private java.lang.String financialAuditNumber;
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.AttendanceType attendanceType;
    private java.math.BigDecimal financialLoss;
    private java.math.BigDecimal travel;
    private java.math.BigDecimal foodAndDrink;
    private java.math.BigDecimal smartcard;
    private java.math.BigDecimal totalDue;
    private java.math.BigDecimal totalPaid;
    private java.math.BigDecimal balanceToPay;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.time.LocalDateTime auditCreatedOn;
}
