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
public class CompleteJurorResponse {

    @JsonProperty("juror_number")
    private java.lang.String jurorNumber;
    @JsonProperty("pool_number")
    private java.lang.String poolNumber;
    @JsonProperty("first_name")
    private java.lang.String firstName;
    @JsonProperty("last_name")
    private java.lang.String lastName;
    @JsonProperty("postcode")
    private java.lang.String postCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("completion_date")
    private java.time.LocalDate completionDate;
}
