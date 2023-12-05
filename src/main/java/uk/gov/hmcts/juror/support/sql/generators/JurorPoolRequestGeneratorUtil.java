package uk.gov.hmcts.juror.support.sql.generators;

import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.sql.dto.CreateJurorPoolRequest;
import uk.gov.hmcts.juror.support.sql.entity.JurorGenerator;

import java.util.List;
import java.util.Map;

public class JurorPoolRequestGeneratorUtil {


    public static List<CreateJurorPoolRequest> createByStatus(Map<String,Integer> statusCountMap,
                                                              int minPoolSizeInclusive,
                                                              int maxPoolSizeExclusive){
return  List.of();
    
    }

    public static CreateJurorPoolRequest responded() {

        new JurorGenerator().setFirstName(new FixedValueGeneratorImpl<>("My Name"));
        return new CreateJurorPoolRequest(
            JurorGeneratorUtil.responded(),
            JurorPoolGeneratorUtil.responded()
        );
    }

    public static CreateJurorPoolRequest disqualified() {
        return new CreateJurorPoolRequest(
            JurorGeneratorUtil.disqualified(),
            JurorPoolGeneratorUtil.disqualified()
        );
    }

    public static CreateJurorPoolRequest excludedJuror() {
        return new CreateJurorPoolRequest(
            JurorGeneratorUtil.excluded(),
            JurorPoolGeneratorUtil.excluded()
        );
    }
}
