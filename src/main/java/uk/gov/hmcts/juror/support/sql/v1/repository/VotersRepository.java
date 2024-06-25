package uk.gov.hmcts.juror.support.sql.v1.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v1.entity.Voters;

@Repository
public interface VotersRepository extends CrudRepository<Voters, Voters.VotersId> {

}
