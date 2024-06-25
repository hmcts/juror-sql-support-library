package uk.gov.hmcts.juror.support.sql.v1.entity;

import lombok.Getter;
@Getter
public enum ExcusableCode {


    CRIMINAL_RECORD("K"),
    //DECEASED("D"),
    //Disabled  OVER_69("V"),
    RECENTLY_SERVED("S"),
    THE_FORCES("F"),
    //Disabled  MEDICAL_PROFESSION("H"),
    //POSTPONEMENT_OF_SERVICE("P"),
    RELIGIOUS_REASONS("R"),
    CHILD_CARE("C"),
    WORK_RELATED("W"),
    MEDICAL("M"),
    TRAVELLING_DIFFICULTIES("T"),
    MOVED_FROM_AREA("A"),
    OTHER("O"),
    STUDENT("B"),
    LANGUAGE_DIFFICULTIES("L"),
    //Disabled  PARLIAMENT_EUROPEAN_ASSEMBLY("E"),
    HOLIDAY("Y"),
    CARER("X"),
    FINANCIAL_HARDSHIP("G"),
    BEREAVEMENT("Z"),
    EXCUSE_BY_BUREAU_TOO_MANY_JURORS("J"),
    ILL("I"),
    MENTAL_HEALTH("N");

    private final String code;

    ExcusableCode(String code) {
        this.code = code;
    }
}
