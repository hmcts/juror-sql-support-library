package uk.gov.hmcts.juror.support.sql.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.entity.PoolComment;

@Repository
public interface PoolCommentRepository extends CrudRepository<PoolComment, Long>,
        QuerydslPredicateExecutor<PoolComment> {

}
