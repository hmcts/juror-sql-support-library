package uk.gov.hmcts.juror.support.sql.v1.entity;

import lombok.Getter;

@Getter
public enum JurorStatus {
    SUMMONED(1),
    RESPONDED(2),
    PANEL(3),
    JUROR(4),
    EXCUSED(5),
    DISQUALIFIED(6),
    DEFERRED(7),
    REASSIGNED(8),
    TRANSFERRED(10),
    ADDITIONAL_INFO(11),
    FAILED_TO_ATTEND(12),
    COMPLETED(13);

    private final int id;

    JurorStatus(int id) {
        this.id = id;
    }
}
