package uk.gov.hmcts.juror.support.sql.v2.spring.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.juror.support.sql.v1.entity.CourtLocation;
import uk.gov.hmcts.juror.support.sql.v2.support.Role;
import uk.gov.hmcts.juror.support.sql.v2.support.UserType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", schema = "juror_mod")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"courts"})
public class UserEntity {

    @Id
    @Column(name = "username", unique = true, length = 20)
    @NotEmpty
    @Size(min = 1, max = 20)
    private String username;


    @Column(name = "email", unique = true)
    @Size(min = 1, max = 200)
    private String email;

    @Column(name = "name", length = 50, nullable = false)
    @NotEmpty
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @JsonProperty("last_logged_in")
    private LocalDateTime lastLoggedIn;


    @Column(name = "approval_limit")
    private BigDecimal approvalLimit;

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(schema = "juror_mod", name = "user_roles", joinColumns = @JoinColumn(name = "username",
        referencedColumnName = "username"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles;


    @JoinTable(
        schema = "juror_mod", name = "user_courts",
        joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"),
        inverseJoinColumns = @JoinColumn(name = "loc_code", referencedColumnName = "loc_code")
    )
    @ManyToMany(fetch = FetchType.EAGER)
    private List<CourtLocation> courts;

}
