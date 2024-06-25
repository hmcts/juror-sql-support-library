package uk.gov.hmcts.juror.support.sql.v1.entity;

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





    @Column(name = "active", nullable = false)
    @FixedValueGenerator("true")
    private boolean active;



    @Version
    @FixedValueGenerator("0")
    private Integer version;


    public User() {

    }

}
