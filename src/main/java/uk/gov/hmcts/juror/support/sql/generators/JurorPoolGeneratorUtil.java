package uk.gov.hmcts.juror.support.sql.generators;

import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalDateGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.NullValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.sql.entity.ExcusableCode;
import uk.gov.hmcts.juror.support.sql.entity.Juror;
import uk.gov.hmcts.juror.support.sql.entity.JurorPoolGenerator;
import uk.gov.hmcts.juror.support.sql.entity.JurorStatus;
import uk.gov.hmcts.juror.support.sql.entity.PoolRequest;
import uk.gov.hmcts.juror.support.sql.service.SqlSupportService;

import java.time.LocalDate;
import java.util.Arrays;

public class JurorPoolGeneratorUtil {


    public static JurorPoolGenerator summoned(boolean isCourtOwned) {
        return createStandard(isCourtOwned, JurorStatus.SUMMONED);
    }

    public static JurorPoolGenerator responded(boolean isCourtOwned) {
        return createStandard(isCourtOwned, JurorStatus.RESPONDED);
    }

    public static JurorPoolGenerator panel(boolean isCourtOwned) {
        return createStandard(isCourtOwned, JurorStatus.PANEL);
    }

    public static JurorPoolGenerator juror(boolean isCourtOwned) {
        return createStandard(isCourtOwned, JurorStatus.JUROR);
    }

    public static JurorPoolGenerator excused(boolean isCourtOwned) {
        JurorPoolGenerator jurorPoolGenerator = createStandard(isCourtOwned, JurorStatus.EXCUSED);
        jurorPoolGenerator.setNextDate(new NullValueGeneratorImpl<>());
        return jurorPoolGenerator;
    }

    public static JurorPoolGenerator disqualified(boolean isCourtOwned) {
        JurorPoolGenerator jurorPoolGenerator = createStandard(isCourtOwned, JurorStatus.DISQUALIFIED);
        jurorPoolGenerator.setNextDate(new NullValueGeneratorImpl<>());
        return jurorPoolGenerator;
    }

    public static JurorPoolGenerator deferred(boolean isCourtOwned) {
        JurorPoolGenerator jurorPoolGenerator = createStandard(isCourtOwned, JurorStatus.DEFERRED);
        //Next date set to date you are deferred too?? -- check
        jurorPoolGenerator.setNextDate(new NullValueGeneratorImpl<>());//Only valid for deferral maintenance
        jurorPoolGenerator.setDeferralCode(new RandomFromCollectionGeneratorImpl<>(
            Arrays.stream(ExcusableCode.values()).map(ExcusableCode::getCode).toList()));
        jurorPoolGenerator.setDeferralDate(new LocalDateGeneratorImpl(
            LocalDate.now().minusDays(200),
            LocalDate.now()));
        //Is active is set to false if you are moved to a new pool else this is still true if you are awaiting new pool

        return jurorPoolGenerator;
    }


    public static JurorPoolGenerator postponed(boolean isCourtOwned) {
        JurorPoolGenerator jurorPoolGenerator = createStandard(isCourtOwned, JurorStatus.DEFERRED);
        //Next date set to date you are deferred too?? -- check
        jurorPoolGenerator.setNextDate(new NullValueGeneratorImpl<>());
        jurorPoolGenerator.setDeferralCode(new FixedValueGeneratorImpl<>("P"));
        jurorPoolGenerator.setDeferralDate(new LocalDateGeneratorImpl(
            LocalDate.now().minusDays(200),
            LocalDate.now()));
        //Is active is set to false if you are moved to a new pool else this is still true if you are awaiting new pool

        return jurorPoolGenerator;
    }

    public static JurorPoolGenerator reassigned(boolean isCourtOwned) {
        JurorPoolGenerator jurorPoolGenerator = createStandard(isCourtOwned, JurorStatus.REASSIGNED);
        jurorPoolGenerator.setNextDate(new NullValueGeneratorImpl<>());
        jurorPoolGenerator.setIsActive(new FixedValueGeneratorImpl<>(false));
        return jurorPoolGenerator;
    }

    public static JurorPoolGenerator transferred(boolean isCourtOwned) {
        JurorPoolGenerator jurorPoolGenerator = createStandard(isCourtOwned, JurorStatus.TRANSFERRED);
        jurorPoolGenerator.setNextDate(new NullValueGeneratorImpl<>());
        jurorPoolGenerator.setTransferDate(
            new LocalDateGeneratorImpl(
                LocalDate.now().minusDays(200),
                LocalDate.now())
        );
        return jurorPoolGenerator;
    }

    public static JurorPoolGenerator additionalInfo(boolean isCourtOwned) {
        return createStandard(isCourtOwned, JurorStatus.ADDITIONAL_INFO);
    }

    public static JurorPoolGenerator failedToAttend(boolean isCourtOwned) {
        JurorPoolGenerator generator = createStandard(isCourtOwned, JurorStatus.FAILED_TO_ATTEND);
        generator.setNoAttendances(new FixedValueGeneratorImpl<>(0));
        generator.setNoAttended(new FixedValueGeneratorImpl<>(0));
        return generator;
    }

    public static JurorPoolGenerator completed(boolean isCourtOwned) {
        JurorPoolGenerator generator = createStandard(isCourtOwned, JurorStatus.COMPLETED);
        generator.setNextDate(new NullValueGeneratorImpl<>());
        return generator;
    }

    private static JurorPoolGenerator createStandard(boolean isCourtOwned, JurorStatus jurorStatus) {
        JurorPoolGenerator generator = new JurorPoolGenerator();
        generator.setStatus(new FixedValueGeneratorImpl<>(jurorStatus.getId()));
        if (isCourtOwned) {
            generator.setOwner(new RandomFromCollectionGeneratorImpl<>(SqlSupportService.getCourtOwners()));
        } else {
            generator.setOwner(new FixedValueGeneratorImpl<>("400"));
        }
        return generator;
    }

    public static JurorPoolGenerator create(Juror juror, PoolRequest pool) {
        return create(new JurorPoolGenerator(), juror, pool);
    }

    public static JurorPoolGenerator create(JurorPoolGenerator jurorPoolGenerator, Juror juror, PoolRequest pool) {
        jurorPoolGenerator.setJurorNumber(new FixedValueGeneratorImpl<>(juror.getJurorNumber()));
        jurorPoolGenerator.setPoolNumber(new FixedValueGeneratorImpl<>(pool.getPoolNumber()));
        jurorPoolGenerator.setOwner(new FixedValueGeneratorImpl<>(pool.getOwner()));
        jurorPoolGenerator.setStartDate(new FixedValueGeneratorImpl<>(pool.getReturnDate()));

        return jurorPoolGenerator;
    }

    public static JurorPoolGenerator create(boolean isCourtOwned, JurorStatus jurorStatus) {
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
