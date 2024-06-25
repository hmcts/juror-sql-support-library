package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.deferralmaintenance;

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
public class ProcessJurorPostponementRequestDto {

    @JsonProperty("juror_numbers")
    private java.util.List<java.lang.String> jurorNumbers;
}
