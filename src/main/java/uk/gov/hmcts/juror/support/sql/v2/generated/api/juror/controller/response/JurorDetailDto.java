package uk.gov.hmcts.juror.support.sql.v2.generated.api.juror.controller.response;

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
public class JurorDetailDto {

    @JsonProperty("jurorNumber")
    private java.lang.String jurorNumber;
    @JsonProperty("title")
    private java.lang.String title;
    @JsonProperty("firstName")
    private java.lang.String firstName;
    @JsonProperty("lastName")
    private java.lang.String lastName;
    @JsonProperty("processingStatus")
    private java.lang.Integer processingStatus;
    @JsonProperty("address")
    private java.lang.String address;
    @JsonProperty("address2")
    private java.lang.String address2;
    @JsonProperty("address3")
    private java.lang.String address3;
    @JsonProperty("address4")
    private java.lang.String address4;
    @JsonProperty("address5")
    private java.lang.String address5;
    @JsonProperty("address6")
    private java.lang.String address6;
    @JsonProperty("postcode")
    private java.lang.String postcode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("hearingDate")
    private java.time.LocalDate hearingDate;
    @JsonProperty("locCode")
    private java.lang.String locCode;
    @JsonProperty("courtName")
    private java.lang.String courtName;
    @JsonProperty("locCourtName")
    private java.lang.String locCourtName;
    @JsonProperty("courtAttendTime")
    private java.lang.String courtAttendTime;
    @JsonProperty("courtAddress1")
    private java.lang.String courtAddress1;
    @JsonProperty("courtAddress2")
    private java.lang.String courtAddress2;
    @JsonProperty("courtAddress3")
    private java.lang.String courtAddress3;
    @JsonProperty("courtAddress4")
    private java.lang.String courtAddress4;
    @JsonProperty("courtAddress5")
    private java.lang.String courtAddress5;
    @JsonProperty("courtAddress6")
    private java.lang.String courtAddress6;
    @JsonProperty("courtPostcode")
    private java.lang.String courtPostcode;
}
