package uk.gov.hmcts.juror.support.sql.v2.spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.AppearanceStage;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.Appearance;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.AppearanceId;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public interface AppearanceRepository extends CrudRepository<Appearance, AppearanceId> {

    List<Appearance> findAllByPoolNumberAndJurorNumberAndAppearanceStage(String poolNumber, String jurorNumber,
                                                                         AppearanceStage stage);

    @Query(value = "SELECT a.* FROM juror_mod.appearance a"
        + " JOIN juror_mod.juror_trial jt on a.juror_number = jt.juror_number and a.pool_number = jt.pool_number "
        + "WHERE a.attendance_date is NULL and jt.result = 'J' and jt.trial_end_date is null",
        nativeQuery = true
    )
    List<Appearance> findAllAttendancesToConfirm();

    @Query(value = "SELECT distinct a.attendance_date FROM juror_mod.appearance a",
        nativeQuery = true
    )
    List<Date> findDistinctAttendanceDate();

    @Query(
        value = "select distinct juror_number from juror_mod.appearance a"
            + " where a.attendance_date = ?1 and a.loc_code = ?2"
            + " and a.appearance_stage= 'CHECKED_OUT'",
        nativeQuery = true
    )
    List<String> findJurorNumbersByDateLocationCode(LocalDate attendanceDate, String locCode);

    @Query(
        value = "select a.juror_number, a.pool_number, a.loc_code, max(a.attendance_date) as max_attendance_date "
            + "from juror_mod.appearance a  "
            + "join juror_mod.juror_pool jp on jp.juror_number = a.juror_number and jp.pool_number = a.pool_number "
            + "where  jp.status = 2 "
            + "and  "
            + "(SELECT count(*) FROM juror_mod.appearance a2  "
            + "where a.juror_number = a2.juror_number   "
            + "and a.pool_number = a2.pool_number   "
            + "and a.attendance_date = a2.attendance_date   "
            + "and a.loc_code = a2.loc_code  "
            + "and (a2.appearance_stage IS  NULL  "
            + "OR a2.appearance_stage = 'CHECKED_IN'  "
            + "OR a2.appearance_stage = 'CHECKED_OUT'  "
            + ")) = 0  "
            + "group by a.juror_number, a.loc_code , a.pool_number",
        nativeQuery = true
    )
    List<Map<String, Objects>> findJurorPoolsForCompletion();

    @Query(
        value = "select * from ( "
            + "select a.pool_number, a.juror_number, a.attendance_date,  "
            + "count(*) as count, "
            + "sum(case when a.appearance_stage  is NULL then 1 else 0 end) as nullStageCount "
            + "from juror_mod.appearance a "
            + "group by a.pool_number, a.juror_number, a.attendance_date) a2 "
            + "where a2.count = 2 "
            + "and a2.nullStageCount = 1",
        nativeQuery = true
    )
    List<Map<String, Object>> getToBeClosed();

    Appearance findAllByPoolNumberAndJurorNumberAndAttendanceDateAndAppearanceStageIsNull(
        String poolNumber, String jurorNumber, LocalDate localDate);

}
