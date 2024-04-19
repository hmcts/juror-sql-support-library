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
public class JurorsToDismissRequestDto {

    @JsonProperty("pool_numbers")
    private java.util.List<java.lang.String> poolNumbers;
    @JsonProperty("location_code")
    private java.lang.String locationCode;
    @JsonProperty("number_of_jurors_to_dismiss")
    private int numberOfJurorsToDismiss;
    @JsonProperty("include_jurors_on_call")
    private java.lang.Boolean includeOnCall;
    @JsonProperty("include_jurors_not_in_attendance")
    private java.lang.Boolean includeNotInAttendance;
}
