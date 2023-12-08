package uk.gov.hmcts.juror.support.sql.entity.jurorresponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.juror.support.sql.entity.AbstractJurorResponse;

@Entity
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "juror_response", schema = "juror_mod")
@EqualsAndHashCode(callSuper = true)
public class PaperResponse extends AbstractJurorResponse {

    @Column(name = "mental_health_capacity")
    private Boolean mentalHealthCapacity;

    @Column(name = "deferral")
    private Boolean deferral;

    @Column(name = "excusal")
    private Boolean excusal;

    @Column(name = "signed")
    private Boolean signed;

    public PaperResponse() {
        super();
        super.setReplyType(new ReplyType("Paper", "Paper response"));
    }
}
