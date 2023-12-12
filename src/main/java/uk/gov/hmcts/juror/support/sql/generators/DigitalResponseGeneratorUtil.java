package uk.gov.hmcts.juror.support.sql.generators;

import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.sql.entity.jurorresponse.DigitalResponseGenerator;
import uk.gov.hmcts.juror.support.sql.entity.JurorStatus;

public abstract class DigitalResponseGeneratorUtil {

    public static DigitalResponseGenerator summoned() {
        DigitalResponseGenerator generator = createStandard();
        JurorResponseGeneratorUtil.summoned(generator);
        return generator;
    }

    public static DigitalResponseGenerator responded() {
        DigitalResponseGenerator generator = createStandard();
        JurorResponseGeneratorUtil.responded(generator);
        return generator;
    }


    public static DigitalResponseGenerator excused() {
        DigitalResponseGenerator generator = createStandard();
        JurorResponseGeneratorUtil.excused(generator);

        return generator;
    }

    public static DigitalResponseGenerator disqualified() {
        DigitalResponseGenerator generator = createStandard();
        JurorResponseGeneratorUtil.disqualified(generator);

        return generator;
    }

    public static DigitalResponseGenerator deferred() {
        DigitalResponseGenerator generator = createStandard();
        JurorResponseGeneratorUtil.deferred(generator);

        return generator;
    }

    public static DigitalResponseGenerator additionalInfo() {
        DigitalResponseGenerator generator = createStandard();
        JurorResponseGeneratorUtil.additionalInfo(generator);
        return generator;
    }


    private static DigitalResponseGenerator createStandard() {
        DigitalResponseGenerator generator = new DigitalResponseGenerator();
        generator.setReplyType(new FixedValueGeneratorImpl<>("Digital"));
        return generator;
    }

    public static DigitalResponseGenerator create(JurorStatus jurorStatus) {
        return switch (jurorStatus) {
            case SUMMONED -> summoned();
            case EXCUSED -> excused();
            case DISQUALIFIED -> disqualified();
            case DEFERRED -> deferred();
            case ADDITIONAL_INFO -> additionalInfo();
            default -> responded();
        };
    }
}
