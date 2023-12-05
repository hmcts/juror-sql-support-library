package uk.gov.hmcts.juror.support.sql.generators;

import uk.gov.hmcts.juror.support.sql.entity.JurorPoolGenerator;

public class JurorPoolGeneratorUtil {
    public static JurorPoolGenerator disqualified() {
        return new JurorPoolGenerator();
    }

    public static JurorPoolGenerator excluded() {
        return new JurorPoolGenerator();
    }

    public static JurorPoolGenerator responded() {
        return new JurorPoolGenerator();
    }
}
