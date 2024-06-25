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
public class DailyExpenseFinancialLoss {

    @JsonProperty("loss_of_earnings")
    private java.math.BigDecimal lossOfEarningsOrBenefits;
    @JsonProperty("extra_care_cost")
    private java.math.BigDecimal extraCareCost;
    @JsonProperty("other_cost")
    private java.math.BigDecimal otherCosts;
    @JsonProperty("other_cost_description")
    private java.lang.String otherCostsDescription;
}
