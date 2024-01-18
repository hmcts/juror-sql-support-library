package uk.gov.hmcts.juror.support.sql.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.entity.User;
import uk.gov.hmcts.juror.support.sql.entity.Voters;

@Repository
public interface VotersRepository extends CrudRepository<Voters, Voters.VotersId> {

}
