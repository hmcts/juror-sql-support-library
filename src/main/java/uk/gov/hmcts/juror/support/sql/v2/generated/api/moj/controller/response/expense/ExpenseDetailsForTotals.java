package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExpenseDetailsForTotals {

    private boolean financialLossApportionedApplied;
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.PayAttendanceType payAttendance;
    private java.math.BigDecimal totalDue;
    private java.math.BigDecimal totalPaid;
}
