package uk.gov.hmcts.juror.support.sql.v1.entity;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum PoliceCheck {
    NOT_CHECKED(false, false, "Not Checked"),
    INSUFFICIENT_INFORMATION(false,false,NOT_CHECKED.getDescription()),
    IN_PROGRESS(false, false, "In Progress"),
    ELIGIBLE(false, true, "Passed"),
    INELIGIBLE(false, true, "Failed"),

    ERROR_RETRY_NAME_HAS_NUMERICS(true, false, IN_PROGRESS.getDescription()),
    ERROR_RETRY_CONNECTION_ERROR(true, false, IN_PROGRESS.getDescription()),
    ERROR_RETRY_OTHER_ERROR_CODE(true, false, IN_PROGRESS.getDescription()),
    ERROR_RETRY_NO_ERROR_REASON(true, false, IN_PROGRESS.getDescription()),
    ERROR_RETRY_UNEXPECTED_EXCEPTION(true, false, IN_PROGRESS.getDescription()),

    UNCHECKED_MAX_RETRIES_EXCEEDED(true, false, "Not Checked - There was a problem");

    private final boolean isError;
    private final boolean isChecked;
    private final String description;

    PoliceCheck(boolean isError, boolean isChecked, String description) {
        this.isError = isError;
        this.isChecked = isChecked;
        this.description = description;
    }
}
