package uk.gov.hmcts.juror.support.sql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.entity.JurorPoolId;

@Repository
public interface JurorPoolRepository extends JpaRepository<JurorPool, JurorPoolId>,
        QuerydslPredicateExecutor<JurorPool> {


}
