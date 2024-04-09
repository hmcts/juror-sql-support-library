package uk.gov.hmcts.juror.support.sql.v1.generators;

import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorStatus;
import uk.gov.hmcts.juror.support.sql.v1.entity.jurorresponse.PaperResponseGenerator;

public abstract class PaperResponseGeneratorUtil {

    public static PaperResponseGenerator summoned() {
        PaperResponseGenerator generator = createStandard();
        JurorResponseGeneratorUtil.summoned(generator);
        return generator;
    }

    public static PaperResponseGenerator responded() {
        PaperResponseGenerator generator = createStandard();
        JurorResponseGeneratorUtil.responded(generator);
        return generator;
    }


    public static PaperResponseGenerator excused() {
        PaperResponseGenerator generator = createStandard();
        JurorResponseGeneratorUtil.excused(generator);
        generator.setExcusal(new FixedValueGeneratorImpl<>(true));
        return generator;
    }

    public static PaperResponseGenerator disqualified() {
        PaperResponseGenerator generator = createStandard();
        JurorResponseGeneratorUtil.disqualified(generator);
        generator.setMentalHealthCapacity(new FixedValueGeneratorImpl<>(true));
        return generator;
    }

    public static PaperResponseGenerator deferred() {
        PaperResponseGenerator generator = createStandard();
        JurorResponseGeneratorUtil.deferred(generator);
        generator.setExcusal(new FixedValueGeneratorImpl<>(true));
        return generator;
    }


    public static PaperResponseGenerator additionalInfo() {
        PaperResponseGenerator generator = createStandard();
        JurorResponseGeneratorUtil.additionalInfo(generator);
        generator.setSigned(new FixedValueGeneratorImpl<>(false));
        return generator;
    }


    private static PaperResponseGenerator createStandard() {
        PaperResponseGenerator generator = new PaperResponseGenerator();
        generator.setReplyType(new FixedValueGeneratorImpl<>("Paper"));
        return generator;
    }

    public static PaperResponseGenerator create(JurorStatus jurorStatus) {
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
