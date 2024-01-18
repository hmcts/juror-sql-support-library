package uk.gov.hmcts.juror.support.sql.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

}
