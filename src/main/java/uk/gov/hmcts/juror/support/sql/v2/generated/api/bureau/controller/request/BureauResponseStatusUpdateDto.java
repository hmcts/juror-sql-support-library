package uk.gov.hmcts.juror.support.sql.v2.generated.api.bureau.controller.request;

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
public class BureauResponseStatusUpdateDto {

    private java.lang.String jurorNumber;
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.juror.domain.ProcessingStatus status;
    private java.lang.Integer version;
}
