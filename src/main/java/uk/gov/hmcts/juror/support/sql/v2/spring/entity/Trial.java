package uk.gov.hmcts.juror.support.sql.v2.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.trial.TrialType;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * juror_mod.trial table entity.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "trial", schema = "juror_mod")
public class Trial implements Serializable {

    @Id
    @Column(name = "trial_number")
//    @Length(max = 16)
    @NotBlank
    private String trialNumber;

    @NotNull
    @Column(name = "loc_code")
    private String locCode;

    @Column(name = "description")
//    @Length(max = 50)
    @NotBlank
    private String description;

    @NotNull
//    @ManyToOne
    @Column(name = "courtroom", nullable = false)
    private long courtroom;

    @NotNull
//    @ManyToOne
    @Column(name = "judge", nullable = false)
    private Long judge;

    @Column(name = "trial_type")
    @NotNull
    @Enumerated(EnumType.STRING)
    private TrialType trialType;

    @Column(name = "trial_start_date")
    private LocalDate trialStartDate;

    @Column(name = "trial_end_date")
    private LocalDate trialEndDate;

    @Column(name = "juror_requested")
    @Deprecated(since = "Old Heritage column")
    private Integer jurorRequested;

    @Column(name = "jurors_sent")
    @Deprecated(since = "Old Heritage column")
    private Integer jurorsSent;

    @Column(name = "anonymous")
    private Boolean anonymous;

}
