package uk.gov.hmcts.juror.support.sql.v1.entity;

import lombok.Getter;

@Getter
public enum ProcessingStatus {
    TODO("To Do"),

    AWAITING_CONTACT("Awaiting Juror"),
    AWAITING_COURT_REPLY("Awaiting Court"),
    AWAITING_TRANSLATION("Awaiting Translation"),

    @Deprecated
    REFERRED_TO_TEAM_LEADER("Referred to Team Leader"), // no longer used in code, but may be in db

    CLOSED("Closed");

    private final String description;

    ProcessingStatus(String description) {
        this.description = description;
    }

}
