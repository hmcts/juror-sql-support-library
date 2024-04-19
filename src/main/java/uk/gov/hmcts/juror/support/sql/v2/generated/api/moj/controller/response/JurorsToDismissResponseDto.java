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
public class JurorsToDismissResponseDto {

    @JsonProperty("jurors_to_dismiss_request_data")
    private java.util.List<JurorsToDismissData> data;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class JurorsToDismissData {

    @JsonProperty("juror_number")
    private java.lang.String jurorNumber;
    @JsonProperty("first_name")
    private java.lang.String firstName;
    @JsonProperty("last_name")
    private java.lang.String lastName;
    @JsonProperty("attending")
    private java.lang.String attending;
    @JsonProperty("check_in_time")
    private java.time.LocalTime checkInTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("next_due_at_court")
    private java.lang.String nextDueAtCourt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("service_start_date")
    private java.time.LocalDate serviceStartDate;
}
}
