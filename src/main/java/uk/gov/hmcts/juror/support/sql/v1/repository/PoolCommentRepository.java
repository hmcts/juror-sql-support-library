package uk.gov.hmcts.juror.support.sql.v1.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v1.entity.PoolComment;

@Repository
public interface PoolCommentRepository extends CrudRepository<PoolComment, Long> {

}
