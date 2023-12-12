package uk.gov.hmcts.juror.support.sql.repository;


import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.entity.jurorresponse.PaperResponse;

@Repository
public interface JurorPaperResponseRepository extends JurorResponseRepository<PaperResponse> {

}
