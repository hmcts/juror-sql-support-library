package uk.gov.hmcts.juror.support.sql.v1.entity.jurorresponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.juror.support.generation.generators.code.GenerateGenerationConfig;
import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGenerator;

@Entity
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "juror_response", schema = "juror_mod")
@EqualsAndHashCode(callSuper = true)
@GenerateGenerationConfig
public class PaperResponse extends AbstractJurorResponse {

    @Column(name = "mental_health_capacity")
    @FixedValueGenerator("false")
    private Boolean mentalHealthCapacity;

    @Column(name = "deferral")
    @FixedValueGenerator("false")
    private Boolean deferral;

    @Column(name = "excusal")
    @FixedValueGenerator("false")
    private Boolean excusal;

    @Column(name = "signed")
    @FixedValueGenerator("true")
    private Boolean signed;

    public PaperResponse() {
        super();
    }
}
