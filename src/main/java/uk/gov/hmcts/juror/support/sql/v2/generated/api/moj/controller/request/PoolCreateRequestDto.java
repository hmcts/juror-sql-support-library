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
public class PoolCreateRequestDto {

    @JsonProperty("poolNumber")
    private java.lang.String poolNumber;
    @JsonProperty("startDate")
    private java.time.LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonProperty("attendTime")
    private java.time.LocalDateTime attendTime;
    @JsonProperty("noRequested")
    private java.lang.Integer noRequested;
    @JsonProperty("bureauDeferrals")
    private java.lang.Integer bureauDeferrals;
    @JsonProperty("numberRequired")
    private java.lang.Integer numberRequired;
    @JsonProperty("citizensToSummon")
    private java.lang.Integer citizensToSummon;
    @JsonProperty("catchmentArea")
    private java.lang.String catchmentArea;
    @JsonProperty("postcodes")
    private java.util.List<java.lang.String> postcodes;
}
