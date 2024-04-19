package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.authentication;

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
public class CourtDto {

    private java.lang.String name;
    private java.lang.String locCode;
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.CourtType courtType;
}
