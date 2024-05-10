package uk.gov.hmcts.juror.support.sql.v2.generation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.UserEntity;
import uk.gov.hmcts.juror.support.sql.v2.support.Role;
import uk.gov.hmcts.juror.support.sql.v2.support.UserType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
public class User {
    @ToString.Include
    final String username;
    final String owner;
    List<String> courts = new ArrayList<>();
    final UserType userType;
    final Collection<Role> roles;

    public User(UserEntity user){
        this(user.getUsername(), user.getOwner(), user.getUserType(), user.getRoles());
    }

    public User addCourt(String court) {
        this.courts.add(court);
        return this;
    }

    public boolean hasRole(Role role) {
        if(this.roles == null){
            return false;
        }
        return this.roles.contains(role);
    }
}