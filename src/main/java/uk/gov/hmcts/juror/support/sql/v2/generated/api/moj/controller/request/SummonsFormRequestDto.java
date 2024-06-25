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
public class SummonsFormRequestDto {

    @JsonProperty("poolNumber")
    private java.lang.String poolNumber;
    @JsonProperty("catchmentArea")
    private java.lang.String catchmentArea;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonProperty("attendTime")
    private java.time.LocalDateTime attendTime;
    @JsonProperty("noRequested")
    private java.lang.Integer noRequested;
    @JsonProperty("nextDate")
    private java.time.LocalDate nextDate;
}
