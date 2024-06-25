package uk.gov.hmcts.juror.support.sql.v2.spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.UserCourtsEntity;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.UserEntity;

import java.util.List;

@Repository
public interface UserV2Repository extends CrudRepository<UserEntity, String> {


    @Query("SELECT u FROM UserCourtsEntity u WHERE u.locCode = :owner")
    List<UserCourtsEntity> findAllWithCourt(String owner);

}
