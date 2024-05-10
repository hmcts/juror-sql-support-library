package uk.gov.hmcts.juror.support.sql.v2.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v1.entity.PoliceCheck;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.RequirePncCheckViewEntity;

import java.util.List;

@Repository
public interface RequirePncCheckViewRepository extends CrudRepository<RequirePncCheckViewEntity, String> {

//    Iterable<RequirePncCheckViewEntity> findAllByPoliceCheckIs(PoliceCheck policeCheck);

    List<RequirePncCheckViewEntity> findAllByPoliceCheckIs(PoliceCheck policeCheck);
}
