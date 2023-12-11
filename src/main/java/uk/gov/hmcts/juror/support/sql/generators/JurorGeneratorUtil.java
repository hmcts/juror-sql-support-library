package uk.gov.hmcts.juror.support.sql.generators;

import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalDateGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.NullValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.sql.entity.DisqualifyCode;
import uk.gov.hmcts.juror.support.sql.entity.ExcusableCode;
import uk.gov.hmcts.juror.support.sql.entity.JurorGenerator;
import uk.gov.hmcts.juror.support.sql.entity.JurorStatus;
import uk.gov.hmcts.juror.support.sql.entity.PoliceCheck;

import java.time.LocalDate;
import java.util.Arrays;

public class JurorGeneratorUtil {

    public static JurorGenerator summoned(boolean isCourtOwned) {
        JurorGenerator generator = createStandard(isCourtOwned);
        generator.setResponded(new FixedValueGeneratorImpl<>(false));
        return generator;
    }

    public static JurorGenerator responded(boolean isCourtOwned) {
        return createStandard(isCourtOwned);
    }

    public static JurorGenerator panel(boolean isCourtOwned) {
        return createStandard(isCourtOwned);
    }

    public static JurorGenerator juror(boolean isCourtOwned) {
        return createStandard(isCourtOwned);
    }

    public static JurorGenerator excused(boolean isCourtOwned) {
        JurorGenerator generator = createStandard(isCourtOwned);
        generator.setExcusalCode(new RandomFromCollectionGeneratorImpl<>(
            Arrays.stream(ExcusableCode.values()).map(ExcusableCode::getCode).toList()
        ));
        generator.setExcusalDate(new LocalDateGeneratorImpl(
            LocalDate.now().minusDays(200),
            LocalDate.now())
        );
        return generator;
    }

    public static JurorGenerator disqualified(boolean isCourtOwned) {
        JurorGenerator generator = createStandard(isCourtOwned);
        generator.setDisqualifyCode(new RandomFromCollectionGeneratorImpl<>(
            Arrays.stream(DisqualifyCode.values()).map(DisqualifyCode::getCode).toList()
        ));
        generator.setDisqualifyDate(new LocalDateGeneratorImpl(
            LocalDate.now().minusDays(200),
            LocalDate.now())
        );
        generator.setNoDefPos(new FixedValueGeneratorImpl<>(1));
        return generator;
    }

    public static JurorGenerator deferred(boolean isCourtOwned) {
        JurorGenerator generator = createStandard(isCourtOwned);
        generator.setNoDefPos(new FixedValueGeneratorImpl<>(1));
        return generator;
    }

    public static JurorGenerator reassigned(boolean isCourtOwned) {
        return createStandard(isCourtOwned);
    }

    public static JurorGenerator transferred(boolean isCourtOwned) {
        return createStandard(isCourtOwned);
    }

    public static JurorGenerator additionalInfo(boolean isCourtOwned) {
        return createStandard(isCourtOwned);
    }

    public static JurorGenerator failedToAttend(boolean isCourtOwned) {
        JurorGenerator generator = createStandard(isCourtOwned);
        generator.setCompletionDate(new NullValueGeneratorImpl<>());
        return generator;
    }

    public static JurorGenerator completed(boolean isCourtOwned) {
        JurorGenerator generator = createStandard(isCourtOwned);
        generator.setCompletionDate(new LocalDateGeneratorImpl(
            LocalDate.now().minusDays(200),
            LocalDate.now())
        );
        return generator;
    }

    private static JurorGenerator createStandard(boolean isCourtOwned) {
        JurorGenerator generator = new JurorGenerator();
        generator.setPoliceCheck(new FixedValueGeneratorImpl<>(PoliceCheck.ELIGIBLE));
        return generator;
    }


    public static JurorGenerator create(boolean isCourtOwned, JurorStatus jurorStatus) {
        return switch (jurorStatus) {
            case SUMMONED -> summoned(isCourtOwned);
            case RESPONDED -> responded(isCourtOwned);
            case PANEL -> panel(isCourtOwned);
            case JUROR -> juror(isCourtOwned);
            case EXCUSED -> excused(isCourtOwned);
            case DISQUALIFIED -> disqualified(isCourtOwned);
            case DEFERRED -> deferred(isCourtOwned);
            case REASSIGNED -> reassigned(isCourtOwned);
            case TRANSFERRED -> transferred(isCourtOwned);
            case ADDITIONAL_INFO -> additionalInfo(isCourtOwned);
            case FAILED_TO_ATTEND -> failedToAttend(isCourtOwned);
            case COMPLETED -> completed(isCourtOwned);
        };
    }
}
