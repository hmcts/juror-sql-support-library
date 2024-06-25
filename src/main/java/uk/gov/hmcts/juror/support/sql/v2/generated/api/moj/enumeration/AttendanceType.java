package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public enum AttendanceType {
    FULL_DAY,
    HALF_DAY,
    FULL_DAY_LONG_TRIAL,
    HALF_DAY_LONG_TRIAL,
    NON_ATTENDANCE_LONG_TRIAL,
    NON_ATTENDANCE,
    ABSENT,
}
