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
public class CoronerPoolRequestDto {

    @JsonProperty("courtCode")
    private java.lang.String locationCode;
    @JsonProperty("noRequested")
    private java.lang.Integer noRequested;
    @JsonProperty("name")
    private java.lang.String name;
    @JsonProperty("emailAddress")
    private java.lang.String emailAddress;
    @JsonProperty("phone")
    private java.lang.String phone;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("requestDate")
    private java.time.LocalDate requestDate;
}
