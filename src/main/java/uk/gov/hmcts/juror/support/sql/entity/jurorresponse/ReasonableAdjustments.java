package uk.gov.hmcts.juror.support.sql.entity.jurorresponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_reasonable_adjustments", schema = "juror_mod")
@Getter
@Setter
public class ReasonableAdjustments {
    @Column(name = "code")
    @Id
    private String code;

    @Column(name = "description")
    private String description;
}