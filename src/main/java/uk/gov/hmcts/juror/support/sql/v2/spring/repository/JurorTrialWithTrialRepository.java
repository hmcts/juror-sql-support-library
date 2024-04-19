package uk.gov.hmcts.juror.support.sql.v2.spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPoolId;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.JurorTrialWithTrial;

@Repository
public interface JurorTrialWithTrialRepository  extends CrudRepository<JurorTrialWithTrial, JurorPoolId> {

    @Query(value = "SELECT jt.*, t.trial_start_date as trial_start_date FROM juror_mod.juror_trial jt"
        + " JOIN juror_mod.trial t ON t.trial_number = jt.trial_number"
        + " WHERE jt.result = 'J' and trial_end_date is null"
        + " ORDER BY jt.trial_number",
        nativeQuery = true
    )
    Iterable<JurorTrialWithTrial> findJurorTrialsJuryNotEnded();

}
