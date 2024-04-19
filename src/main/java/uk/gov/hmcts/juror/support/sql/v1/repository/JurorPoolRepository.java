package uk.gov.hmcts.juror.support.sql.v1.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPoolId;
import uk.gov.hmcts.juror.support.sql.v1.entity.ProcessingStatus;

import java.util.List;

@Repository
public interface JurorPoolRepository extends CrudRepository<JurorPool, JurorPoolId> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE juror_mod.juror_pool CASCADE", nativeQuery = true)
    void truncateTable();

    List<JurorPool> findAllByPoolNumber(String poolNumber);

    @Query(value = "SELECT jp.*, p.loc_code as loc_code FROM juror_mod.juror_pool jp"
        + " JOIN juror_mod.pool p ON p.pool_no = jp.pool_number "
        + "WHERE jp.juror_number = ?2 AND jp.pool_number = ?1",
        nativeQuery = true
    )
    JurorPool findByPoolNumberAndJurorNumber(String poolNumber, String jurorNumber);

    @Query(value = "SELECT jp.*, p.loc_code as loc_code FROM juror_mod.juror_pool jp"
        + " JOIN juror_mod.pool p ON p.pool_no = jp.pool_number "
        + "LEFT JOIN juror_mod.juror_trial jt on jp.juror_number = jt.juror_number "
        + "WHERE jp.status = ?1 AND jp.next_date IS NOT NULL AND jp.owner != '400' "
        + "AND jt.juror_number IS NULL "
        + "ORDER BY jp.next_date , p.loc_code ",
        nativeQuery = true
    )
    Iterable<JurorPool> findJurorPoolsByStatusOrdered(int status);

    @Query(value = "SELECT jp.*, p.loc_code as loc_code FROM juror_mod.juror_pool jp"
        + " JOIN juror_mod.pool p ON p.pool_no = jp.pool_number "
//        + "LEFT JOIN juror_mod.juror_trial jt on jp.juror_number = jt.juror_number "
        + "LEFT JOIN juror_mod.appearance a on a.juror_number = jp.juror_number and a.pool_number = jp.pool_number "
        + "and a.trial_number is not NULL "
        + "WHERE jp.status = 2 AND jp.next_date IS NOT NULL AND jp.owner != '400' and a.attendance_date is NULL "
//        + "AND jt.juror_number IS NULL "
        + "ORDER BY jp.next_date , p.loc_code ",
        nativeQuery = true
    )
    List<JurorPool> findJurorPoolsNotInJury();

    List<JurorPool> findAllByStatus(int completed);
}
