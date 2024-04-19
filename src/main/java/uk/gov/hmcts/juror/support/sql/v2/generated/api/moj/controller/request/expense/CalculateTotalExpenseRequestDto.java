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
public class CalculateTotalExpenseRequestDto {

    private java.lang.String jurorNumber;
    @JsonProperty("pool_number")
    private java.lang.String poolNumber;
    private java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpense> expenseList;
}
