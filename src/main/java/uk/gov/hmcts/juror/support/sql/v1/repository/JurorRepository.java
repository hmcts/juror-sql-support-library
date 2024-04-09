package uk.gov.hmcts.juror.support.sql.v1.repository;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v1.entity.Juror;

@Repository
public interface JurorRepository extends CrudRepository<Juror, String> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE juror_mod.juror CASCADE", nativeQuery = true)
    void truncateTable();
}
