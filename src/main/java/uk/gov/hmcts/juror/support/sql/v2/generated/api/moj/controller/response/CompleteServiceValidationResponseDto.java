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
public class CompleteServiceValidationResponseDto {

    @JsonProperty("valid")
    private java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.JurorStatusValidationResponseDto> valid;
    @JsonProperty("invalid_not_responded")
    private java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.JurorStatusValidationResponseDto> invalidNotResponded;
}
