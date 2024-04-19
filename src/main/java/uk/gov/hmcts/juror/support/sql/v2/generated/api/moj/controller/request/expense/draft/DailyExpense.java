package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft;

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
public class DailyExpense {

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("date_of_expense")
    private java.time.LocalDate dateOfExpense;
    @JsonProperty("pool_number")
    private java.lang.String poolNumber;
    @JsonProperty("pay_cash")
    private java.lang.Boolean payCash;
    @JsonProperty("time")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseTime time;
    @JsonProperty("financial_loss")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseFinancialLoss financialLoss;
    @JsonProperty("travel")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseTravel travel;
    @JsonProperty("food_and_drink")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseFoodAndDrink foodAndDrink;
    @JsonProperty("apply_to_days")
    private java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseApplyToAllDays> applyToAllDays;
}
