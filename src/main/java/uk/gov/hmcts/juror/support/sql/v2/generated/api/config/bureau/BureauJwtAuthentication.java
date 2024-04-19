package uk.gov.hmcts.juror.support.sql.v2.generated.api.config.bureau;

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
public class BureauJwtAuthentication {

    private java.lang.String token;
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.config.bureau.BureauJwtPayload principal;
}
