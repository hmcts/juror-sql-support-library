package uk.gov.hmcts.juror.support.sql.v1.repository;


import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.v1.entity.jurorresponse.PaperResponse;

@Repository
public interface JurorPaperResponseRepository extends JurorResponseRepository<PaperResponse> {

}
