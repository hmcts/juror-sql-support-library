package uk.gov.hmcts.juror.support.sql.v2.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.Trial;

@Repository
public interface TrialRepository extends CrudRepository<Trial, String> {


    Trial findByTrialNumber(String trialNumber);

}
