package uk.gov.hmcts.juror.support.sql.v2.spring.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ToBeClosed {
    @Column(name = "juror_number")
    private String jurorNumber;

    @Column(name = "pool_number")
    private String poolNumber;

    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    private String locCode;

    private long count;
    private long nullStageCount;
    public ToBeClosed(Map<String, Object> dataMap) {
        this.jurorNumber = String.valueOf(dataMap.get("juror_number"));
        this.poolNumber = String.valueOf(dataMap.get("pool_number"));
        this.attendanceDate =
            LocalDate.parse(String.valueOf(dataMap.get("attendance_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.count = Long.parseLong(String.valueOf(dataMap.get("count")));
        this.nullStageCount = Long.parseLong(String.valueOf(dataMap.get("nullStageCount")));
    }
}
