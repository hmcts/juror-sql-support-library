package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@SuppressWarnings("squid:NoSonar")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CombinedExpenseDetailsDto<T> {


    private List<T> expenseDetails;

    private Total total;

    public CombinedExpenseDetailsDto() {
        this(false);
    }

    public CombinedExpenseDetailsDto(boolean hasTotals) {
        expenseDetails = new ArrayList<>();
        total = new Total(hasTotals);
    }


    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    @ToString(callSuper = true)
    public static class Total extends ExpenseValuesDto {

        private int totalDays;
        private BigDecimal totalDue;
        private BigDecimal totalPaid;

        @JsonIgnore
        private boolean hasTotals;

        public Total() {
            this(false);
        }

        public Total(boolean hasTotals) {
            super();
            totalDays = 0;
            this.hasTotals = hasTotals;
        }



        @JsonProperty("total_outstanding")
        @NotNull
        public BigDecimal getTotalOutstanding() {
            if (hasTotals) {
                return getTotalDue()
                    .subtract(getTotalPaid());
            }
            return null;
        }

        @JsonProperty("total_due")
        public BigDecimal getTotalDue() {
            if (hasTotals) {
                return Optional.ofNullable(totalDue).orElse(BigDecimal.ZERO);
            }
            return null;
        }

        @JsonProperty("total_paid")
        public BigDecimal getTotalPaid() {
            if (hasTotals) {
                return Optional.ofNullable(totalPaid).orElse(BigDecimal.ZERO);
            }
            return null;
        }
    }
}
