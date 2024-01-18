package uk.gov.hmcts.juror.support.sql.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.type.YesNoConverter;
import uk.gov.hmcts.juror.support.generation.generators.code.GenerateGenerationConfig;
import uk.gov.hmcts.juror.support.generation.generators.value.DateFilter;
import uk.gov.hmcts.juror.support.generation.generators.value.DateTimeFilter;
import uk.gov.hmcts.juror.support.generation.generators.value.FirstNameGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalDateTimeGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.TimeFilter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Entity
@Table(name = "users", schema = "juror_mod")
@Data
@Builder
@AllArgsConstructor
@GenerateGenerationConfig
public class User {

    @Column(name = "owner")
    private String owner;

    @Id
    @Column(name = "username", unique = true, length = 20)
    private String username;

    @Column(name = "name", length = 50, nullable = false)
    @FirstNameGenerator
    private String name;


    @Column(name = "level", nullable = false)
    //Level 0 = Standard User
    //Level 1 = Team Leader
    //Level 9 = Senior Juror Officer
    @FixedValueGenerator("0")
    private Integer level;


    @Column(name = "active", nullable = false)
    @FixedValueGenerator("true")
    private boolean active;

    @JsonProperty("last_logged_in")
    @LocalDateTimeGenerator(
        minInclusive = @DateTimeFilter(
            dateFilter = @DateFilter(mode = DateFilter.Mode.MINUS, value = 1, unit = ChronoUnit.DAYS),
            timeFilter = @TimeFilter(mode = TimeFilter.Mode.MINUS, value = 10, unit = ChronoUnit.MINUTES)
        ),
        maxExclusive = @DateTimeFilter(
            dateFilter = @DateFilter(mode = DateFilter.Mode.MINUS, value = 0, unit = ChronoUnit.DAYS),
            timeFilter = @TimeFilter(mode = TimeFilter.Mode.MINUS, value = 0, unit = ChronoUnit.MINUTES)
        )
    )
    private LocalDateTime lastLoggedIn;

    @Version
    @FixedValueGenerator("0")
    private Integer version;

    //Temp fields until we migrate over to active directory
    @Column(name = "password")
    @JsonIgnore
    @Deprecated(forRemoval = true)
    @FixedValueGenerator("5BAA61E4C9B93F3F")
    private String password;
    @Transient
    @Column(name = "password_warning")
    @Deprecated(forRemoval = true)
    private Boolean passwordWarning;

    @Transient
    @Column(name = "days_to_expire")
    @Deprecated(forRemoval = true)
    @FixedValueGenerator("97")
    private Integer daysToExpire;

    @Column(name = "password_changed_date")
    @Deprecated(forRemoval = true)
    private Date passwordChangedDate;

    @Column(name = "failed_login_attempts")
    @Deprecated(forRemoval = true)
    @FixedValueGenerator("0")
    private int failedLoginAttempts;

    @Column(name = "LOGIN_ENABLED_YN")
    @Convert(converter = YesNoConverter.class)
    @Deprecated(forRemoval = true)
    @FixedValueGenerator("true")
    private Boolean loginEnabledYn;


    public User() {

    }

}
