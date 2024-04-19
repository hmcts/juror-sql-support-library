package uk.gov.hmcts.juror.support.sql.v2.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPoolId;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "juror_trial", schema = "juror_mod")
@IdClass(JurorPoolId.class)
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
@Slf4j
public class JurorTrialWithTrial implements Serializable {

    @Id
    @Column(name = "juror_number")
    private String jurorNumber;

    @Id
    @Column(name = "pool_number")
    private String poolNumber;

    @NotNull
    @Column(name = "loc_code")
    private String locCode;

    @Column(name = "trial_number")
    private String trialNumber;


    @Column(name = "completed")
    private Boolean completed;

    @Column(name = "result")
    private String result;

    @Column(name = "trial_start_date")
    private LocalDate trialStartDate;

}
