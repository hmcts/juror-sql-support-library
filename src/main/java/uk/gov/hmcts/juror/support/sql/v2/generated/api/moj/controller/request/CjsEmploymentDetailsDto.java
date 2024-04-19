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
public class CjsEmploymentDetailsDto {

    @JsonProperty("cjsEmployment")
    private java.util.List<CjsEmployment> cjsEmployment;
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
}
