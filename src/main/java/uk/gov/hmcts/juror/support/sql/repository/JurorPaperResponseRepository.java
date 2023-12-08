package uk.gov.hmcts.juror.support.sql.repository;


import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.entity.jurorresponse.PaperResponse;
import uk.gov.hmcts.juror.support.sql.entity.jurorresponse.ReplyType;

@Repository
public interface JurorPaperResponseRepository extends JurorResponseRepository<PaperResponse> {
    PaperResponse findByJurorNumberAndReplyType(String jurorNumber, ReplyType replyType);

    default PaperResponse findByJurorNumber(String jurorNumber) {
        return findByJurorNumberAndReplyType(jurorNumber, new ReplyType("Paper", null));
    }
}
