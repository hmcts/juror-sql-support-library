package uk.gov.hmcts.juror.support.sql.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DisqualifyCode {

    AGE("A", "Age"),
    BAIL("B", "Bail"),
    CONVICTION("C", "Conviction"),
    MENTAL_HEALTH_ACT("M", "Mental Health Act"),
    RESIDENCY("R", "Residency"),
    ELECTRONIC_POLICE_CHECK_FAILURE("E", "Electronic Police check Failure"),
    JUDICIAL_DISQUALIFICATION("D","JUDICIAL DISQUALIFICATION")
    ;

    private final String code;
    private final String description;
    }

