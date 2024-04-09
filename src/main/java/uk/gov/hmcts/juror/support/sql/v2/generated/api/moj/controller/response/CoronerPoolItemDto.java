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
public class CoronerPoolItemDto {

    @JsonProperty("poolNumber")
    private java.lang.String poolNumber;
    @JsonProperty("courtName")
    private java.lang.String courtName;
    @JsonProperty("locCode")
    private java.lang.String locCode;
    @JsonProperty("noRequested")
    private java.lang.Integer noRequested;
    @JsonProperty("totalAdded")
    private java.lang.Integer totalAdded;
    @JsonProperty("name")
    private java.lang.String name;
    @JsonProperty("emailAddress")
    private java.lang.String emailAddress;
    @JsonProperty("phone")
    private java.lang.String phone;
    @JsonProperty("dateRequested")
    private java.time.LocalDate dateRequested;
    @JsonProperty("coronerDetailsList")
    private java.util.List<CoronerDetails> coronerDetailsList;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class CoronerDetails {

    @JsonProperty("jurorNumber")
    private java.lang.String jurorNumber;
    @JsonProperty("title")
    private java.lang.String title;
    @JsonProperty("firstName")
    private java.lang.String firstName;
    @JsonProperty("lastName")
    private java.lang.String lastName;
    @JsonProperty("addressLineOne")
    private java.lang.String addressLineOne;
    @JsonProperty("addressLineTwo")
    private java.lang.String addressLineTwo;
    @JsonProperty("addressLineThree")
    private java.lang.String addressLineThree;
    @JsonProperty("addressLineFour")
    private java.lang.String addressLineFour;
    @JsonProperty("addressLineFive")
    private java.lang.String addressLineFive;
    @JsonProperty("addressLineSix")
    private java.lang.String addressLineSix;
    @JsonProperty("postcode")
    private java.lang.String postcode;
}
}
