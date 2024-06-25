package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public enum AppearanceStage {
    CHECKED_IN,
    CHECKED_OUT,
    EXPENSE_ENTERED,
    EXPENSE_AUTHORISED,
    EXPENSE_EDITED,
}
