package uk.gov.hmcts.juror.support.sql.v2.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.JudgeEntity;

import java.util.List;

@Repository
public interface JudgeRepository  extends CrudRepository<JudgeEntity, Long> {

    List<JudgeEntity> findAllByOwner(String owner);
}
