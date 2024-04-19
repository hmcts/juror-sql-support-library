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
public class CombinedSimplifiedExpenseDetailDto {

    @JsonProperty("expense_details")
    private java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.SimplifiedExpenseDetailDto> expenseDetails;
    @JsonProperty("total")
    private Total total;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class Total {

    @JsonProperty("total_attendances")
    private int totalAttendances;
    @JsonProperty("financial_loss")
    private java.math.BigDecimal financialLoss;
    @JsonProperty("travel")
    private java.math.BigDecimal travel;
    @JsonProperty("food_and_drink")
    private java.math.BigDecimal foodAndDrink;
    @JsonProperty("smartcard")
    private java.math.BigDecimal smartcard;
    @JsonProperty("total_due")
    private java.math.BigDecimal totalDue;
    @JsonProperty("total_paid")
    private java.math.BigDecimal totalPaid;
    @JsonProperty("balance_to_pay")
    private java.math.BigDecimal balanceToPay;
}
}
