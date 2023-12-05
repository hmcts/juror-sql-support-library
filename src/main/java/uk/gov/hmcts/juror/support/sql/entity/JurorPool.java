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

//@Entity
//@Table(name = "pool", schema = "juror_mod")
@NoArgsConstructor
@Getter
@Setter
@ToString
@GenerateGenerationConfig
public class JurorPool implements Serializable {

    @Id
    //@Audited
    //@NotBlank
    @Column(name = "juror_number")
    //@Pattern(regexp = JUROR_NUMBER)
    //@Length(max = 9)
    private String poolNumber;


    private String jurorNumber;
}
