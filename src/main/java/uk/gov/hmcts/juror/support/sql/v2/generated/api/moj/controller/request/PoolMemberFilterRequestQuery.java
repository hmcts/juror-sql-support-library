package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PoolMemberFilterRequestQuery {

    @JsonProperty("pool_number")
    private java.lang.String poolNumber;
    @JsonProperty("juror_number")
    private java.lang.String jurorNumber;
    @JsonProperty("first_name")
    private java.lang.String firstName;
    @JsonProperty("last_name")
    private java.lang.String lastName;
    @JsonProperty("attendance")
    private java.util.List<AttendanceEnum> attendance;
    @JsonProperty("checked_in")
    private java.lang.Boolean checkedIn;
    @JsonProperty("next_due")
    private java.util.List<java.lang.String> nextDue;
    @JsonProperty("status")
    private java.util.List<java.lang.String> statuses;
    @JsonProperty("sort_method")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.SortMethod sortMethod;
    @JsonProperty("sort_field")
    private SortField sortField;
    @JsonProperty("page_limit")
    private long pageLimit;
    @JsonProperty("page_number")
    private long pageNumber;
public enum AttendanceEnum {
    ON_CALL,
    ON_A_TRIAL,
    IN_ATTENDANCE,
    OTHER,
}
public enum SortField {
    JUROR_NUMBER,
    FIRST_NAME,
    LAST_NAME,
    NEXT_DATE,
    POSTCODE,
    ATTENDANCE,
    CHECKED_IN,
    STATUS,
}
}
