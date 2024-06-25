package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.summonsmanagement;

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
public class JurorResponseRetrieveRequestDto {

    @JsonProperty("juror_number")
    private java.lang.String jurorNumber;
    @JsonProperty("pool_number")
    private java.lang.String poolNumber;
    @JsonProperty("last_name")
    private java.lang.String lastName;
    @JsonProperty("officer_assigned")
    private java.lang.String officerAssigned;
    @JsonProperty("is_urgent")
    private java.lang.Boolean isUrgent;
    @JsonProperty("processing_status")
    private java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.juror.domain.ProcessingStatus> processingStatus;
}
