package uk.gov.hmcts.juror.support.sql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.gov.hmcts.juror.support.generation.generators.code.GenerateGenerationConfig;
import uk.gov.hmcts.juror.support.generation.generators.value.SequenceGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.StringSequenceGenerator;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

//@Entity
//@Table(name = "pool", schema = "juror_mod")
@NoArgsConstructor
@Getter
@Setter
@ToString
@GenerateGenerationConfig
public class Pool implements Serializable {

    @Id
    //@Audited
    //@NotBlank
    @Column(name = "pool_no")
    //@Pattern(regexp = JUROR_NUMBER)
    //@Length(max = 9)
    @StringSequenceGenerator(format = "%09d",
        sequenceGenerator = @SequenceGenerator(id = "pool_number", start = 1)
    )
    private String poolNumber;
}
