package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.summonsmanagement;

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
public class DisqualifyReasonsDto {

    @JsonProperty("disqualifyReasons")
    private java.util.List<DisqualifyReasons> disqualifyReasons;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class DisqualifyReasons {

    @JsonProperty("code")
    private java.lang.String code;
    @JsonProperty("description")
    private java.lang.String description;
    @JsonProperty("heritageCode")
    private java.lang.String heritageCode;
    @JsonProperty("heritageDescription")
    private java.lang.String heritageDescription;
}
}
