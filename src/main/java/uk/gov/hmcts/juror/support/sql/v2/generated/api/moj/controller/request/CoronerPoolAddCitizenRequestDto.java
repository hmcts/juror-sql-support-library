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
public class CoronerPoolAddCitizenRequestDto {

    @JsonProperty("poolNumber")
    private java.lang.String poolNumber;
    @JsonProperty("locCode")
    private java.lang.String locCode;
    @JsonProperty("postcodeAndNumbers")
    private java.util.List<PostCodeAndNumbers> postcodeAndNumbers;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class PostCodeAndNumbers {

    @JsonProperty("postcode")
    private java.lang.String postcode;
    @JsonProperty("numberToAdd")
    private java.lang.Integer numberToAdd;
}
}
