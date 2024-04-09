package uk.gov.hmcts.juror.support.sql.v1.repository;

import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v1.entity.jurorresponse.DigitalResponse;

@Repository
public interface JurorDigitalResponseRepository extends JurorResponseRepository<DigitalResponse> {

}