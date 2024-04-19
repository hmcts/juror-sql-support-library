package uk.gov.hmcts.juror.support.sql.v2.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.CourtRoomEntity;

import java.util.List;

@Repository
public interface CourtroomRepository extends CrudRepository<CourtRoomEntity, Long> {
    List<CourtRoomEntity> findByCourtLocationOwner(String courtCode);
}
