package uk.gov.hmcts.juror.support.sql.generators;

import uk.gov.hmcts.juror.support.sql.entity.JurorGenerator;

public class JurorGeneratorUtil {


    public static JurorGenerator responded() {
        return new JurorGenerator();
    }

    public static JurorGenerator disqualified() {
        return new JurorGenerator();
    }

    public static JurorGenerator excluded() {
        return new JurorGenerator();
    }
}
