package uk.gov.hmcts.juror.support.sql.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import uk.gov.hmcts.juror.support.sql.v1.entity.PoolRequestGenerator;

import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class CreatePopulatedPoolRequest {
    private final PoolRequestGenerator poolRequestGenerator;
    private final Map<CreateJurorPoolRequest, Integer> jurorCountMap;


}
