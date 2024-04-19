package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompleteServiceJurorNumberListDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("completion_date")
    private java.time.LocalDate completionDate;

    @NotEmpty
    @JsonProperty("juror_numbers")
    private List<String> jurorNumbers;
}
