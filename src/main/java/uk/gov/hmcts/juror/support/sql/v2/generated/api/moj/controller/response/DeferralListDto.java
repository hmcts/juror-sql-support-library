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
public class DeferralListDto {

    @JsonProperty("deferrals")
    private java.util.List<DeferralListDataDto> deferrals;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class DeferralListDataDto {

    @JsonProperty("courtLocation")
    private java.lang.String courtLocation;
    @JsonProperty("jurorNumber")
    private java.lang.String jurorNumber;
    @JsonProperty("firstName")
    private java.lang.String firstName;
    @JsonProperty("lastName")
    private java.lang.String lastName;
    @JsonProperty("poolNumber")
    private java.lang.String poolNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("deferredTo")
    private java.time.LocalDate deferredTo;
}
}
