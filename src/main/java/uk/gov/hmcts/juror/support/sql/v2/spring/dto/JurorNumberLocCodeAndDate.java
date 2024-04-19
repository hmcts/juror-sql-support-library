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
public class JurorNumberLocCodeAndDate {
    @Column(name = "juror_number")
    private String jurorNumber;

    @Column(name = "pool_number")
    private String poolNumber;

    @Column(name = "loc_code")
    private String locCode;

    @Column(name = "max_attendance_date")
    private LocalDate maxAttendanceDate;

    public JurorNumberLocCodeAndDate(Map<String, Objects> dataMap) {
        this.jurorNumber = String.valueOf(dataMap.get("juror_number"));
        this.poolNumber = String.valueOf(dataMap.get("pool_number"));
        this.locCode = String.valueOf(dataMap.get("loc_code"));
        this.maxAttendanceDate =
            LocalDate.parse(String.valueOf(dataMap.get("max_attendance_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
