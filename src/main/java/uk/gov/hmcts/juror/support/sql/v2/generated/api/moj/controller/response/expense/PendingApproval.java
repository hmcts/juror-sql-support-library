package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PendingApproval {
    private String jurorNumber;

    private String poolNumber;
    private java.lang.String firstName;
    private java.lang.String lastName;
    private java.math.BigDecimal amountDue;
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ExpenseType expenseType;
    private boolean canApprove;
    @JsonProperty("revisions")
    private java.util.List<DateToRevision> dateToRevisions;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class DateToRevision {

        @JsonFormat(pattern = "yyyy-MM-dd")
        @JsonProperty("attendance_date")
        private java.time.LocalDate attendanceDate;
        private java.lang.Long version;
    }
}
