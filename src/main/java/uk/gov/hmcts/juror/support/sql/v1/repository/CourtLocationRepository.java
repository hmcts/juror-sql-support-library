package uk.gov.hmcts.juror.support.sql.v1.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v1.entity.CourtLocation;

import java.util.List;

@Repository
public interface CourtLocationRepository extends CrudRepository<CourtLocation, String> {

    @Query("SELECT c FROM CourtLocation c WHERE c.owner = c.locCode")
    List<CourtLocation> findAllByOwnerEqualsLocCode();

    List<CourtLocation> findAllByOwner(String owner);
}
