package uk.gov.hmcts.juror.support.sql.v2.generation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.juror.support.sql.v1.entity.CourtLocation;

@RequiredArgsConstructor
@Getter
@Builder
public class CourtRoom {
    final long id;
    final CourtLocation courtLocation;
    final String roomNumber;
    final String description;
}