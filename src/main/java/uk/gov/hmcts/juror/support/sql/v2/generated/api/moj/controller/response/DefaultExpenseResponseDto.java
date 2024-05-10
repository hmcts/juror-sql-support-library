package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response;

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
public class DefaultExpenseResponseDto {

    @JsonProperty("financial_loss")
    private java.math.BigDecimal financialLoss;
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("travel_time")
    private java.time.LocalTime travelTime;
    @JsonProperty("mileage")
    private java.lang.Integer distanceTraveledMiles;
    @JsonProperty("smart_card")
    private java.lang.String smartCardNumber;
    @JsonProperty("claiming_subsistence_allowance")
    private boolean claimingSubsistenceAllowance;
}
