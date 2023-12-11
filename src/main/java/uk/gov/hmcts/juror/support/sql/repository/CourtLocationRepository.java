package uk.gov.hmcts.juror.support.sql.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.entity.CourtLocation;

@Repository
public interface CourtLocationRepository extends CrudRepository<CourtLocation, String> {

}
