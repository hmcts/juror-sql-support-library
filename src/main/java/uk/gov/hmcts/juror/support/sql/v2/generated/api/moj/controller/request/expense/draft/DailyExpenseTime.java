package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft;

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
public class DailyExpenseTime {

    @JsonProperty("pay_attendance")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.PayAttendanceType payAttendance;
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("travel_time")
    private java.time.LocalTime travelTime;
}
