package uk.gov.hmcts.juror.support.sql.v1.generators;

import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.sql.v1.entity.UserGenerator;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class UserGeneratorUtil {

    private static final AtomicLong counter = new AtomicLong(0);

    public static UserGenerator standardCourtUser(String owner) {
        return createStandard(owner, 0);
    }

    public static UserGenerator seniorJurorOfficerCourtUser(String owner) {
        return createStandard(owner, 9);
    }

    public static UserGenerator standardBureauUser() {
        return createStandard("400", 0);
    }

    public static UserGenerator teamLeadBureauUser() {
        return createStandard("400", 1);
    }

    private static UserGenerator createStandard(String owner, int level) {
        UserGenerator generator = new UserGenerator();
        generator.setOwner(new FixedValueGeneratorImpl<>(owner));
        generator.addPostGenerate(user -> {
            user.setUsername(owner + "_LVL_" + level + "_ID_" + counter.getAndIncrement());
        });
        return generator;
    }
}
