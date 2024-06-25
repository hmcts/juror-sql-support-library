package uk.gov.hmcts.juror.support.sql.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.juror.support.sql.v1.entity.Voters;
import uk.gov.hmcts.juror.support.sql.v1.entity.jurorresponse.AbstractJurorResponse;
import uk.gov.hmcts.juror.support.sql.v1.entity.Juror;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPool;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class JurorAccountDetailsDto {
    private final Juror juror;
    private AbstractJurorResponse jurorResponse;
    private List<JurorPool> jurorPools;
    private Voters voters;
}
