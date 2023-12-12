package uk.gov.hmcts.juror.support.sql.generators;

import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.FormattedLocalDateGeneratorImpl;
import uk.gov.hmcts.juror.support.sql.entity.JurorStatus;
import uk.gov.hmcts.juror.support.sql.entity.jurorresponse.DigitalResponseGenerator;

import java.time.LocalDate;

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
        generator.setExcusalReason(new FixedValueGeneratorImpl<>("Child care"));
        JurorResponseGeneratorUtil.excused(generator);
        return generator;
    }

    public static DigitalResponseGenerator disqualified() {
        DigitalResponseGenerator generator = createStandard();
        generator.setBail(new FixedValueGeneratorImpl<>(true));

        JurorResponseGeneratorUtil.disqualified(generator);
        return generator;
    }

    public static DigitalResponseGenerator deferred() {
        DigitalResponseGenerator generator = createStandard();
        generator.setDeferralReason(new FixedValueGeneratorImpl<>("Holiday"));
        generator.setDeferralDate(
            new FormattedLocalDateGeneratorImpl(
                "dd-MM-YYYY",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(180)
            ));
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
