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
public class PoolRequestItemDto {

    @JsonProperty("poolNumber")
    private java.lang.String poolNumber;
    @JsonProperty("newRequest")
    private java.lang.Character newRequest;
    @JsonProperty("courtName")
    private java.lang.String courtName;
    @JsonProperty("locCode")
    private java.lang.String locCode;
    @JsonProperty("attendTime")
    private java.time.LocalDateTime attendTime;
    @JsonProperty("additionalSummons")
    private java.lang.Integer additionalSummons;
    @JsonProperty("courtSupplied")
    private java.lang.Integer courtSupplied;
    @JsonProperty("noRequested")
    private java.lang.Integer noRequested;
    @JsonProperty("totalRequired")
    private java.lang.Integer totalRequired;
    @JsonProperty("lastUpdate")
    private java.time.LocalDateTime lastUpdate;
    @JsonProperty("nextDate")
    private java.time.LocalDate nextDate;
    @JsonProperty("returnDate")
    private java.time.LocalDate returnDate;
}
