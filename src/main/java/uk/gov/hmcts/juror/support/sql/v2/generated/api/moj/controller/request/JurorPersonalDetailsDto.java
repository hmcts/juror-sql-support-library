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
public class JurorPersonalDetailsDto {

    @JsonProperty("replyMethod")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.ReplyMethod replyMethod;
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
    @JsonProperty("addressTown")
    private java.lang.String addressTown;
    @JsonProperty("addressCounty")
    private java.lang.String addressCounty;
    @JsonProperty("addressPostcode")
    private java.lang.String addressPostcode;
    @JsonProperty("dateOfBirth")
    private java.time.LocalDate dateOfBirth;
    @JsonProperty("primaryPhone")
    private java.lang.String primaryPhone;
    @JsonProperty("secondaryPhone")
    private java.lang.String secondaryPhone;
    @JsonProperty("emailAddress")
    private java.lang.String emailAddress;
    @JsonProperty("thirdParty")
    private ThirdParty thirdParty;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class ThirdParty {

    @JsonProperty("relationship")
    private java.lang.String relationship;
    @JsonProperty("thirdPartyReason")
    private java.lang.String thirdPartyReason;
}
}
