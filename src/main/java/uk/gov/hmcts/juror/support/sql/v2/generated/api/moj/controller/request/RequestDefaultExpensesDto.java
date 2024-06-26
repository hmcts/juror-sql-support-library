package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request;

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
public class RequestDefaultExpensesDto {

    @JsonProperty("financial_loss")
    private java.math.BigDecimal financialLoss;
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("travel_time")
    private java.time.LocalTime travelTime;
    @JsonProperty("mileage")
    private int distanceTraveledMiles;
    @JsonProperty("smart_card")
    private java.lang.String smartCardNumber;
    @JsonProperty("apply_to_all_draft")
    private boolean overwriteExistingDraftExpenses;
    @JsonProperty("food_and_drink")
    private boolean hasFoodAndDrink;
}
