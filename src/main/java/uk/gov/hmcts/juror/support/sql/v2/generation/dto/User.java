package uk.gov.hmcts.juror.support.sql.v2.generation.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import uk.gov.hmcts.juror.support.sql.v1.entity.CourtLocation;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.UserCourtsEntity;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.UserEntity;
import uk.gov.hmcts.juror.support.sql.v2.support.Role;
import uk.gov.hmcts.juror.support.sql.v2.support.UserType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @ToString.Include
    @EqualsAndHashCode.Include
    final String username;
    @ToString.Include
    final String owner;
    final List<String> courts = new ArrayList<>();
    final UserType userType;
    final Collection<Role> roles;
    @Setter
    private String activeLocCode;

    public User(UserEntity user) {
        this(user.getUsername(), String.valueOf(user.getCourts().get(0)), user.getCourts()
            .stream().map(CourtLocation::getLocCode).toList(),
            user.getUserType(), user.getRoles());
    }

    public User(UserCourtsEntity user) {
        this(user.getUsername(), String.valueOf(user.getUserEntity().getCourts().get(0)), user.getUserEntity().getCourts()
                .stream().map(CourtLocation::getLocCode).toList(),
            user.getUserEntity().getUserType(), user.getUserEntity().getRoles());
    }

    public User(String username, String courtCode, List<String> locCodes, UserType userType, Set<Role> roles) {
        this(username, courtCode, userType, roles, courtCode);
        if (locCodes != null) {
            this.courts.addAll(locCodes);
        }
    }

    public User addCourt(String court) {
        this.courts.add(court);
        return this;
    }

    public boolean hasRole(Role role) {
        if (this.roles == null) {
            return false;
        }
        return this.roles.contains(role);
    }

}