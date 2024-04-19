package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.juror.support.sql.v1.entity.PoliceCheck;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class PoliceCheckStatusDto {
    private PoliceCheck status;
}
