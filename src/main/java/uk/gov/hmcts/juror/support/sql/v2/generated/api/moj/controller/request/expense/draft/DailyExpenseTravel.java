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
public class DailyExpenseTravel {

    @JsonProperty("traveled_by_car")
    private java.lang.Boolean traveledByCar;
    @JsonProperty("jurors_taken_by_car")
    private java.lang.Integer jurorsTakenCar;
    @JsonProperty("traveled_by_motorcycle")
    private java.lang.Boolean traveledByMotorcycle;
    @JsonProperty("jurors_taken_by_motorcycle")
    private java.lang.Integer jurorsTakenMotorcycle;
    @JsonProperty("traveled_by_bicycle")
    private java.lang.Boolean traveledByBicycle;
    @JsonProperty("miles_traveled")
    private java.lang.Integer milesTraveled;
    @JsonProperty("parking")
    private java.math.BigDecimal parking;
    @JsonProperty("public_transport")
    private java.math.BigDecimal publicTransport;
    @JsonProperty("taxi")
    private java.math.BigDecimal taxi;
}
