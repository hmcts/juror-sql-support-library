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
public class PoolAdditionalSummonsDto {

    @JsonProperty("poolNumber")
    private java.lang.String poolNumber;
    @JsonProperty("noRequested")
    private java.lang.Integer noRequested;
    @JsonProperty("bureauDeferrals")
    private java.lang.Integer bureauDeferrals;
    @JsonProperty("citizensSummoned")
    private java.lang.Integer citizensSummoned;
    @JsonProperty("citizensToSummon")
    private java.lang.Integer citizensToSummon;
    private java.lang.String catchmentArea;
    @JsonProperty("postcodes")
    private java.util.List<java.lang.String> postcodes;
}
