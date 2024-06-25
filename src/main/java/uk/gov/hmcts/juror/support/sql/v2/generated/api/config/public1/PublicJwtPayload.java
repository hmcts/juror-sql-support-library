package uk.gov.hmcts.juror.support.sql.v2.generated.api.config.public1;

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
public class PublicJwtPayload {

    private java.lang.String id;
    private java.lang.String jurorNumber;
    private java.lang.String surname;
    private java.lang.String postcode;
    private java.lang.String[] roles;
}
