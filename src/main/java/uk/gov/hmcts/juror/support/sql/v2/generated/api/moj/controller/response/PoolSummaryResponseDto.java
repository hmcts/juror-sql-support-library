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
public class PoolSummaryResponseDto {

    @JsonProperty("poolDetails")
    private PoolDetails poolDetails;
    @JsonProperty("bureauSummoning")
    private BureauSummoning bureauSummoning;
    @JsonProperty("poolSummary")
    private PoolSummary poolSummary;
    @JsonProperty("additionalStatistics")
    private AdditionalStatistics additionalStatistics;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class PoolSummary {

    @JsonProperty("currentPoolSize")
    private int currentPoolSize;
    @JsonProperty("requiredPoolSize")
    private int requiredPoolSize;
}
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class PoolDetails {

    @JsonProperty("poolNumber")
    private java.lang.String poolNumber;
    @JsonProperty("courtName")
    private java.lang.String courtName;
    @JsonProperty("locCode")
    private java.lang.String courtLocationCode;
    @JsonProperty("courtStartDate")
    private java.lang.String courtStartDate;
    @JsonProperty("additionalRequirements")
    private java.lang.String additionalRequirements;
    @JsonProperty("isActive")
    private java.lang.Boolean isActive;
    @JsonProperty("is_nil_pool")
    private boolean isNilPool;
}
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class AdditionalStatistics {

    @JsonProperty("courtSupply")
    private int courtSupply;
}
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class BureauSummoning {

    @JsonProperty("required")
    private java.lang.Integer requestedFromBureau;
    @JsonProperty("confirmed")
    private int confirmedFromBureau;
    @JsonProperty("unavailable")
    private int unavailable;
    @JsonProperty("notResponded")
    private int unresolved;
    @JsonProperty("surplus")
    private int surplus;
    @JsonProperty("totalSummoned")
    private int totalSummoned;
}
}
