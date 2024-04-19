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
public class JurorPaperResponseDetailDto {

    @JsonProperty("jurorNumber")
    private java.lang.String jurorNumber;
    @JsonProperty("poolNumber")
    private java.lang.String poolNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("serviceStartDate")
    private java.time.LocalDate serviceStartDate;
    @JsonProperty("courtName")
    private java.lang.String courtName;
    @JsonProperty("welshCourt")
    private boolean welshCourt;
    @JsonProperty("jurorStatus")
    private java.lang.String jurorStatus;
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
    @JsonProperty("existingTitle")
    private java.lang.String existingTitle;
    @JsonProperty("existingFirstName")
    private java.lang.String existingFirstName;
    @JsonProperty("existingLastName")
    private java.lang.String existingLastName;
    @JsonProperty("existingAddressLineOne")
    private java.lang.String existingAddressLineOne;
    @JsonProperty("existingAddressLineTwo")
    private java.lang.String existingAddressLineTwo;
    @JsonProperty("existingAddressLineThree")
    private java.lang.String existingAddressLineThree;
    @JsonProperty("existingAddressTown")
    private java.lang.String existingAddressTown;
    @JsonProperty("existingAddressCounty")
    private java.lang.String existingAddressCounty;
    @JsonProperty("existingAddressPostcode")
    private java.lang.String existingAddressPostcode;
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    @JsonProperty("deferral")
    private java.lang.Boolean deferral;
    @JsonProperty("excusal")
    private java.lang.Boolean excusal;
    @JsonProperty("excusalReason")
    private java.lang.String excusalReason;
    @JsonProperty("eligibility")
    private Eligibility eligibility;
    @JsonProperty("signed")
    private java.lang.Boolean signed;
    @JsonProperty("thirdParty")
    private ThirdParty thirdParty;
    @JsonProperty("welsh")
    private java.lang.Boolean welsh;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("dateReceived")
    private java.time.LocalDate dateReceived;
    @JsonProperty("processingStatus")
    private java.lang.String processingStatus;
    @JsonProperty("opticReference")
    private java.lang.String opticReference;
    @JsonProperty("superUrgent")
    private java.lang.Boolean superUrgent;
    @JsonProperty("assignedStaffMember")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.authentication.UserDetailsDto assignedStaffMember;
    @JsonProperty("contactLog")
    private java.util.List<ContactLogDataDto> contactLog;
    @JsonProperty("notes")
    private java.lang.String notes;
    @JsonProperty("current_owner")
    private java.lang.String currentOwner;
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
public static class ContactLogDataDto {

    @JsonProperty("username")
    private java.lang.String username;
    @JsonProperty("logDate")
    private java.lang.String logDate;
    @JsonProperty("enquiryType")
    private java.lang.String enquiryType;
    @JsonProperty("notes")
    private java.lang.String notes;
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
}
