package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial;

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
public class JurorDetailRequestDto {

    @JsonProperty("juror_number")
    private java.lang.String jurorNumber;
    @JsonProperty("first_name")
    private java.lang.String firstName;
    @JsonProperty("last_name")
    private java.lang.String lastName;
    @JsonProperty("empanel_status")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.trial.PanelResult result;
}
