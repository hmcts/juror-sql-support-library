package uk.gov.hmcts.juror.support.sql.v2.generation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
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
public class User {
    @ToString.Include
    final String username;
    final String owner;
    final List<String> courts = new ArrayList<>();
    final UserType userType;
    final Collection<Role> roles;
    private String activeLocCode;

    public User(UserEntity user) {
        this(user.getUsername(), user.getOwner(), user.getUserType(), user.getRoles(), user.getOwner());
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

    public void setActiveLocCode(String locCode) {
        this.activeLocCode = locCode;
    }
}