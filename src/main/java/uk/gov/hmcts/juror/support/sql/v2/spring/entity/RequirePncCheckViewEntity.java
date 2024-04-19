package uk.gov.hmcts.juror.support.sql.v2.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import uk.gov.hmcts.juror.support.sql.v1.entity.PoliceCheck;

@Entity
@Getter
@Table(name = "require_pnc_check_view", schema = "juror_mod")
public class RequirePncCheckViewEntity {


    @Id
    @Column(name = "juror_number")
    private String jurorNumber;

    @Column(name = "police_check")
    private PoliceCheck policeCheck;
}
