package uk.gov.hmcts.juror.support.sql.v2.generated.api.juror.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public enum ProcessingStatus {
    TODO,
    AWAITING_CONTACT,
    AWAITING_COURT_REPLY,
    AWAITING_TRANSLATION,
    CLOSED,
    REFERRED_TO_TEAM_LEADER,
}
