package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "A List of available jurors for generating a panel")
public class AvailableJurorsDto {
    @JsonProperty("pool_number")
//    @PoolNumber
    private String poolNumber;

    @JsonProperty("available_jurors")
    @NotNull
    private Long availableJurors;

    @JsonProperty("service_start_date")
    @NotNull
    private LocalDate serviceStartDate;

    @JsonProperty("court_location")
    private String courtLocation;

    @JsonProperty("court_location_code")
//    @Length(min = 3, max = 3)
    private String courtLocationCode;
}
