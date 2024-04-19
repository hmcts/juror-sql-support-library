package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExpenseItemsDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.List<java.time.LocalDate> attendanceDates;


    @NotBlank
    private String jurorNumber;


    @NotBlank
    private String poolNumber;
}


