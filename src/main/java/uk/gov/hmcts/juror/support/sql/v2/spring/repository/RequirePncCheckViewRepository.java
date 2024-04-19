package uk.gov.hmcts.juror.support.sql.v2.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.RequirePncCheckViewEntity;

@Repository
public interface RequirePncCheckViewRepository extends CrudRepository<RequirePncCheckViewEntity, String> {

    Iterable<RequirePncCheckViewEntity> findAllByPoliceCheckIsNull();
}
