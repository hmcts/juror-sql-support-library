package uk.gov.hmcts.juror.support.sql.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import uk.gov.hmcts.juror.support.sql.entity.JurorGenerator;
import uk.gov.hmcts.juror.support.sql.entity.JurorPoolGenerator;

@AllArgsConstructor
@Builder
@Getter
public class CreateJurorPoolRequest {
    private final JurorGenerator jurorGenerator;
    private final JurorPoolGenerator jurorPoolGenerator;

}
