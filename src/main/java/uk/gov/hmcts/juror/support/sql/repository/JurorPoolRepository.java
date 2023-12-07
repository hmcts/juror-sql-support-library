package uk.gov.hmcts.juror.support.sql.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.entity.JurorPoolId;

@Repository
public interface JurorPoolRepository extends CrudRepository<JurorPool, JurorPoolId> {

}
