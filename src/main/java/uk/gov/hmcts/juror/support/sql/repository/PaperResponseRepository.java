package uk.gov.hmcts.juror.support.sql.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.entity.jurorresponse.PaperResponse;

@Repository
public interface PaperResponseRepository extends CrudRepository<PaperResponse, String> {

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE juror_mod.juror_response CASCADE", nativeQuery = true)
    void truncateTable();
}
