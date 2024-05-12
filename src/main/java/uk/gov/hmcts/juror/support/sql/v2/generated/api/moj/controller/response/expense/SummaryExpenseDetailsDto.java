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
public class SummaryExpenseDetailsDto {

    private java.math.BigDecimal totalDraft;
    private java.math.BigDecimal totalForApproval;
    private java.math.BigDecimal totalApproved;
    private java.math.BigDecimal totalSmartCard;
}
