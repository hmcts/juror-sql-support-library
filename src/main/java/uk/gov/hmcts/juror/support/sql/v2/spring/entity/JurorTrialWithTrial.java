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

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "juror_trial", schema = "juror_mod")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
@Slf4j
@IdClass(JurorTrialWithTrial.JurorTrialWithTrialId.class)
public class JurorTrialWithTrial implements Serializable {

    public static class JurorTrialWithTrialId {
        @Column(name = "juror_number")
        @Id
        private String jurorNumber;
        @Column(name = "trial_number")
        @Id
        private String trialNumber;
    }

    @Column(name = "date_selected")
    @NotNull
    private LocalDateTime dateSelected;

    /**
     * /* The date the juror started sitting on the trial.
     */
    @Column(name = "empanelled_date")
    private LocalDate empanelledDate;

    /**
     * /* The date the juror was returned from the trial and no longer sitting.
     */
    @Column(name = "return_date")
    private LocalDate returnDate;


    @Column(name = "juror_number")
    @Id
    private String jurorNumber;


    @NotNull
    @Column(name = "loc_code")
    private String locCode;

    @Column(name = "trial_number")
    @Id
    private String trialNumber;


    @Column(name = "completed")
    private Boolean completed;

    @Column(name = "result")
    private String result;
}
