package uk.gov.hmcts.juror.support.sql.v2.generated.api.juror.controller.request;

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
public class JurorResponseDto {

    private java.lang.String jurorNumber;
    private java.lang.String title;
    private java.lang.String firstName;
    private java.lang.String lastName;
    private java.lang.String addressLineOne;
    private java.lang.String addressLineTwo;
    private java.lang.String addressLineThree;
    private java.lang.String addressTown;
    private java.lang.String addressCounty;
    private java.lang.String addressPostcode;
    private java.time.LocalDate dateOfBirth;
    private java.lang.String primaryPhone;
    private java.lang.String secondaryPhone;
    private java.lang.String emailAddress;
    private java.util.List<CjsEmployment> cjsEmployment;
    private java.util.List<ReasonableAdjustment> specialNeeds;
    private java.lang.String assistanceSpecialArrangements;
    private Deferral deferral;
    private Excusal excusal;
    private Qualify qualify;
    private java.lang.Integer version;
    @JsonProperty("replyMethod")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.ReplyMethod replyMethod;
    private ThirdParty thirdParty;
    private java.lang.Boolean welsh;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Deferral {

        private java.lang.String reason;
        private java.lang.String dates;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class CjsEmployment {

        private java.lang.String cjsEmployer;
        private java.lang.String cjsEmployerDetails;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Excusal {

        private java.lang.String reason;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Qualify {

        private Answerable livedConsecutive;
        private Answerable mentalHealthAct;
        private Answerable onBail;
        private Answerable convicted;

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        @Builder
        public static class Answerable {

            private boolean answer;
            private java.lang.String details;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class ReasonableAdjustment {

        private java.lang.String assistanceType;
        private java.lang.String assistanceTypeDetails;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class ThirdParty {

        private java.lang.String thirdPartyFName;
        private java.lang.String thirdPartyLName;
        private java.lang.String relationship;
        private java.lang.String contactEmail;
        private java.lang.String mainPhone;
        private java.lang.String otherPhone;
        private java.lang.String emailAddress;
        private java.lang.String thirdPartyReason;
        private java.lang.String thirdPartyOtherReason;
        private java.lang.Boolean useJurorEmailDetails;
        private java.lang.Boolean useJurorPhoneDetails;
    }
}
