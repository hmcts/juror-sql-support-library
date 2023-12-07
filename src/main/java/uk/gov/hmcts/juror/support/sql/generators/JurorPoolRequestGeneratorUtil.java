package uk.gov.hmcts.juror.support.sql.generators;

import uk.gov.hmcts.juror.support.sql.dto.CreateJurorPoolRequest;

public class JurorPoolRequestGeneratorUtil {



    public static CreateJurorPoolRequest responded(boolean isCourtOwned) {
        return new CreateJurorPoolRequest(
            JurorGeneratorUtil.responded(isCourtOwned),
            JurorPoolGeneratorUtil.responded(isCourtOwned)
        );
    }

    public static CreateJurorPoolRequest disqualified(boolean isCourtOwned) {
        return new CreateJurorPoolRequest(
            JurorGeneratorUtil.disqualified(isCourtOwned),
            JurorPoolGeneratorUtil.disqualified(isCourtOwned)
        );
    }

    public static CreateJurorPoolRequest excusedJuror(boolean isCourtOwned) {
        return new CreateJurorPoolRequest(
            JurorGeneratorUtil.excused(isCourtOwned),
            JurorPoolGeneratorUtil.excused(isCourtOwned)
        );
    }


}
