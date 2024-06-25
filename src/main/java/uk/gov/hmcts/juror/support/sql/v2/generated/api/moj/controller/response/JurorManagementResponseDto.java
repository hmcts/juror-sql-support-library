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
public class JurorManagementResponseDto {

    @JsonProperty("availableForMove")
    private java.util.List<java.lang.String> availableForMove;
    @JsonProperty("unavailableForMove")
    private java.util.List<ValidationFailure> unavailableForMove;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class ValidationFailure {

    @JsonProperty("jurorNumber")
    private java.lang.String jurorNumber;
    @JsonProperty("failureReason")
    private java.lang.String failureReason;
    @JsonProperty("first_name")
    private java.lang.String firstName;
    @JsonProperty("last_name")
    private java.lang.String lastName;
}
}
