package uk.gov.hmcts.juror.support.sql.repository;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.entity.PoolRequest;

@Repository
public interface PoolRequestRepository extends CrudRepository<PoolRequest, String> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE juror_mod.pool CASCADE", nativeQuery = true)
    void truncateTable();
}
