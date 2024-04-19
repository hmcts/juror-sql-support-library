package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApproveExpenseDto {


    private String jurorNumber;

    private String poolNumber;
    @NotNull
    private ApprovalType approvalType;

    @JsonProperty("is_cash_payment")
    @NotNull
    private Boolean cashPayment;

    @JsonProperty("revisions")
    @NotEmpty
    private List<@NotNull DateToRevision> dateToRevisions;


    @Data
    @Builder
    public static class DateToRevision {
        @JsonProperty("attendance_date")
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
        @NotNull
        private LocalDate attendanceDate;
        @NotNull
        private Long version;
    }

    public enum ApprovalType {
        FOR_APPROVAL,
        FOR_REAPPROVAL;

    }
}
