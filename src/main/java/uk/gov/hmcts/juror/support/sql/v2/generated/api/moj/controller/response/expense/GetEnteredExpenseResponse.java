package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseFinancialLoss;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseFoodAndDrink;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseTime;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseTravel;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.AppearanceStage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetEnteredExpenseResponse {
    @JsonProperty("date_of_expense")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfExpense;

    @JsonProperty("none_attendance_day")
    private Boolean noneAttendanceDay;

    @JsonProperty("appearance_stage")
    private AppearanceStage stage;

    @JsonProperty("total_due")
    private BigDecimal totalDue;

    @JsonProperty("total_paid")
    private BigDecimal totalPaid;

    @JsonProperty("pay_cash")
    private Boolean payCash;

    @JsonProperty("time")
    private DailyExpenseTimeEntered time;

    @JsonProperty("financial_loss")
    private DailyExpenseFinancialLoss financialLoss;

    @JsonProperty("travel")
    private DailyExpenseTravel travel;

    @JsonProperty("food_and_drink")
    private DailyExpenseFoodAndDrink foodAndDrink;



    @Getter
    @NoArgsConstructor
    public static class DailyExpenseTimeEntered extends DailyExpenseTime {
        @JsonProperty("time_spent_at_court")
        @JsonFormat(pattern = "HH:mm")
        private LocalTime timeSpentAtCourt;
    }
}
