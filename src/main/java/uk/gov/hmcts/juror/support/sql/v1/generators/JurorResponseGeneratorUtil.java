package uk.gov.hmcts.juror.support.sql.v1.generators;

import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalDateGeneratorImpl;
import uk.gov.hmcts.juror.support.sql.v1.entity.jurorresponse.AbstractJurorResponseGenerator;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorStatus;
import uk.gov.hmcts.juror.support.sql.v1.entity.ProcessingStatus;

import java.time.LocalDate;

public abstract class JurorResponseGeneratorUtil {

    public static void summoned(AbstractJurorResponseGenerator<?> generator) {
        applyStandard(generator);
    }

    public static void responded(AbstractJurorResponseGenerator<?> generator) {
        applyStandard(generator);//TODO Happy
        applyClosed(generator);
    }


    public static void excused(AbstractJurorResponseGenerator<?> generator) {
        applyStandard(generator);
        applyClosed(generator);
    }

    public static void disqualified(AbstractJurorResponseGenerator<?> generator) {
        applyStandard(generator);
        applyClosed(generator);
    }

    public static void deferred(AbstractJurorResponseGenerator<?> generator) {
        applyStandard(generator);
        applyClosed(generator);
    }

    public static void additionalInfo(AbstractJurorResponseGenerator<?> generator) {
        applyStandard(generator);
        generator.setProcessingStatus(new FixedValueGeneratorImpl<>(ProcessingStatus.AWAITING_CONTACT));
    }

    private static void applyStandard(AbstractJurorResponseGenerator<?> generator) {

    }
    public static void applyClosed(AbstractJurorResponseGenerator<?> generator){
        generator.setProcessingStatus(new FixedValueGeneratorImpl<>(ProcessingStatus.CLOSED));
        generator.setCompletedAt(new LocalDateGeneratorImpl(
            LocalDate.now().minusDays(200),
            LocalDate.now())
        );
        generator.setProcessingComplete(new FixedValueGeneratorImpl<>(true));//TODO confirm
    }

    public static void apply(AbstractJurorResponseGenerator<?> generator, JurorStatus jurorStatus) {
        switch (jurorStatus) {
            case SUMMONED -> summoned(generator);
            case EXCUSED -> excused(generator);
            case DISQUALIFIED -> disqualified(generator);
            case DEFERRED -> deferred(generator);
            case ADDITIONAL_INFO -> additionalInfo(generator);
            default -> responded(generator);
        }
    }
}
