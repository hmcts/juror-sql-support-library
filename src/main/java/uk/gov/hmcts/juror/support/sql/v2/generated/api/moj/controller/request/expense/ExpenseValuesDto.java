package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Optional;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@SuppressWarnings("PMD.LawOfDemeter")
public class ExpenseValuesDto {

    protected BigDecimal lossOfEarnings;
    protected BigDecimal extraCare;
    protected BigDecimal other;
    protected BigDecimal publicTransport;
    protected BigDecimal taxi;
    protected BigDecimal motorcycle;
    protected BigDecimal car;
    protected BigDecimal bicycle;
    protected BigDecimal parking;
    protected BigDecimal foodAndDrink;
    protected BigDecimal smartCard;


    @JsonProperty("total")
    public BigDecimal getTotal() {
        return BigDecimal.ZERO
            .add(getLossOfEarnings())
            .add(getExtraCare())
            .add(getOther())
            .add(getPublicTransport())
            .add(getTaxi())
            .add(getMotorcycle())
            .add(getCar())
            .add(getBicycle())
            .add(getParking())
            .add(getFoodAndDrink())
            .subtract(getSmartCard());
    }

    public BigDecimal getLossOfEarnings() {
        return Optional.ofNullable(lossOfEarnings).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getExtraCare() {
        return Optional.ofNullable(extraCare).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getOther() {
        return Optional.ofNullable(other).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getPublicTransport() {
        return Optional.ofNullable(publicTransport).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getTaxi() {
        return Optional.ofNullable(taxi).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getMotorcycle() {
        return Optional.ofNullable(motorcycle).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getCar() {
        return Optional.ofNullable(car).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getBicycle() {
        return Optional.ofNullable(bicycle).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getParking() {
        return Optional.ofNullable(parking).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getFoodAndDrink() {
        return Optional.ofNullable(foodAndDrink).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getSmartCard() {
        return Optional.ofNullable(smartCard).orElse(BigDecimal.ZERO);
    }
}
