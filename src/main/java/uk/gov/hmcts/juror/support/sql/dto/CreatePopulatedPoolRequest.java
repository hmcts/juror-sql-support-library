package uk.gov.hmcts.juror.support.sql.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import uk.gov.hmcts.juror.support.sql.entity.JurorGenerator;
import uk.gov.hmcts.juror.support.sql.entity.PoolGenerator;

import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class CreatePopulatedPoolRequest {
    private final PoolGenerator poolGenerator;
    private final Map<CreateJurorPoolRequest, Integer> jurorCountMap;


}
