package uk.gov.hmcts.juror.support.sql.repository;

import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.entity.jurorresponse.DigitalResponse;

@Repository
public interface JurorDigitalResponseRepository extends JurorResponseRepository<DigitalResponse> {

}