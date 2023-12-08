package uk.gov.hmcts.juror.support.sql.repository;


import org.springframework.stereotype.Repository;
import uk.gov.hmcts.juror.support.sql.entity.jurorresponse.DigitalResponse;
import uk.gov.hmcts.juror.support.sql.entity.jurorresponse.ReplyType;

@Repository
public interface JurorDigitalResponseRepository extends JurorResponseRepository<DigitalResponse> {
    DigitalResponse findByJurorNumberAndReplyType(String jurorNumber, ReplyType replyType);

    default DigitalResponse findByJurorNumber(String jurorNumber) {
        return findByJurorNumberAndReplyType(jurorNumber, new ReplyType("Digital", null));
    }
}