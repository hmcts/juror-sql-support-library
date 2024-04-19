package uk.gov.hmcts.juror.support.sql.v2.spring.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class AppearanceId implements Serializable {

    private String jurorNumber;

    private LocalDate attendanceDate;

    private String locCode;
}
