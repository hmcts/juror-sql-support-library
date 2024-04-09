package uk.gov.hmcts.juror.support.sql.v1.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPoolId;

import java.util.List;

@Repository
public interface JurorPoolRepository extends CrudRepository<JurorPool, JurorPoolId> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE juror_mod.juror_pool CASCADE", nativeQuery = true)
    void truncateTable();

    List<JurorPool> findAllByPoolNumber(String poolNumber);
}
