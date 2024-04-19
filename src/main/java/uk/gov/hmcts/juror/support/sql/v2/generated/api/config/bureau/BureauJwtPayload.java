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
public class BureauJwtPayload {

    private java.lang.String owner;
    private java.lang.String locCode;
    private java.lang.String login;
    private java.lang.String userLevel;
    private java.lang.Boolean passwordWarning;
    private java.lang.Integer daysToExpire;
    private Staff staff;
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.UserType userType;
    private java.util.Collection<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.Role> roles;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public static class Staff {

    private java.lang.String name;
    private java.lang.Integer rank;
    private java.lang.Integer active;
    private java.util.List<java.lang.String> courts;
}
}
