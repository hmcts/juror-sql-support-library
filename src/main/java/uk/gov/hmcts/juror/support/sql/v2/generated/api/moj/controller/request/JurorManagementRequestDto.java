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
public class JurorManagementRequestDto {

    @JsonProperty("sourcePoolNumber")
    private java.lang.String sourcePoolNumber;
    @JsonProperty("sourceCourtLocCode")
    private java.lang.String sourceCourtLocCode;
    @JsonProperty("jurorNumbers")
    private java.util.List<java.lang.String> jurorNumbers;
    @JsonProperty("receivingPoolNumber")
    private java.lang.String receivingPoolNumber;
    @JsonProperty("receivingCourtLocCode")
    private java.lang.String receivingCourtLocCode;
    @JsonProperty("targetServiceStartDate")
    private java.time.LocalDate serviceStartDate;
    @JsonProperty("sendingCourtLocCode")
    private java.lang.String sendingCourtLocCode;
}
