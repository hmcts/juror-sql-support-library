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
public class UserDetailsDto {

    private java.lang.String username;
    private java.lang.String email;
    private java.lang.String name;
    private java.lang.Boolean isActive;
    private java.time.LocalDateTime lastSignIn;
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.UserType userType;
    private java.util.Set<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.Role> roles;
    private java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.authentication.UserCourtDto> courts;
}
