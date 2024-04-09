package uk.gov.hmcts.juror.support.sql.v2.spring.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v1.entity.Voters;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.VotersV2;

@Repository
public interface VotersV2Repository extends CrudRepository<VotersV2, VotersV2.VotersId> {

}
