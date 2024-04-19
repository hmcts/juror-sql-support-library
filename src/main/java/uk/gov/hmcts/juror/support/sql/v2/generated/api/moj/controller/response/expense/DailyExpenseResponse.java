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
public class DailyExpenseResponse {

    @JsonProperty("financial_loss_warning")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.FinancialLossWarning financialLossWarning;
}
