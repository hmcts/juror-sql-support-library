package uk.gov.hmcts.juror.support.sql.v1.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v1.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

}
