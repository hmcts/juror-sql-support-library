package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain;

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
public class FilterPoolMember {

    @JsonProperty("juror_number")
    private java.lang.String jurorNumber;
    @JsonProperty("first_name")
    private java.lang.String firstName;
    @JsonProperty("last_name")
    private java.lang.String lastName;
    @JsonProperty("postcode")
    private java.lang.String postcode;
    @JsonProperty("attendance")
    private java.lang.String attendance;
    @JsonProperty("checked_in")
    private java.time.LocalTime checkedIn;
    @JsonProperty("checked_in_today")
    private java.lang.Boolean checkedInToday;
    @JsonProperty("next_date")
    private java.time.LocalDate nextDate;
    @JsonProperty("status")
    private java.lang.String status;
}
