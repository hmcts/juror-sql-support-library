package uk.gov.hmcts.juror.support.sql.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.juror.support.sql.entity.jurorresponse.AbstractJurorResponse;
import uk.gov.hmcts.juror.support.sql.entity.Juror;
import uk.gov.hmcts.juror.support.sql.entity.JurorPool;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class JurorAccountDetailsDto {
    private final Juror juror;
    private AbstractJurorResponse jurorResponse;
    private List<JurorPool> jurorPools;
}
