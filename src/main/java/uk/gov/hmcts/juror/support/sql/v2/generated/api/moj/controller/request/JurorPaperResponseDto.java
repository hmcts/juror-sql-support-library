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
public class JurorPaperResponseDto {

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
    @JsonProperty("cjsEmployment")
    private java.util.List<CjsEmployment> cjsEmployment;
    @JsonProperty("specialNeeds")
    private java.util.List<ReasonableAdjustment> reasonableAdjustments;
    @JsonProperty("canServeOnSummonsDate")
    private java.lang.Boolean canServeOnSummonsDate;
    @JsonProperty("deferral")
    private java.lang.Boolean deferral;
    @JsonProperty("excusal")
    private java.lang.Boolean excusal;
    @JsonProperty("eligibility")
    private Eligibility eligibility;
    @JsonProperty("signed")
    private java.lang.Boolean signed;
    @JsonProperty("thirdParty")
    private ThirdParty thirdParty;
    @JsonProperty("welsh")
    private java.lang.Boolean welsh;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class CjsEmployment {

    @JsonProperty("cjsEmployer")
    private java.lang.String cjsEmployer;
    @JsonProperty("cjsEmployerDetails")
    private java.lang.String cjsEmployerDetails;
}
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
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class Eligibility {

    @JsonProperty("livedConsecutive")
    private java.lang.Boolean livedConsecutive;
    @JsonProperty("mentalHealthAct")
    private java.lang.Boolean mentalHealthAct;
    @JsonProperty("mentalHealthCapacity")
    private java.lang.Boolean mentalHealthCapacity;
    @JsonProperty("onBail")
    private java.lang.Boolean onBail;
    @JsonProperty("convicted")
    private java.lang.Boolean convicted;
}
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class ReasonableAdjustment {

    @JsonProperty("assistanceType")
    private java.lang.String assistanceType;
    @JsonProperty("assistanceTypeDetails")
    private java.lang.String assistanceTypeDetails;
}
}
