package uk.gov.hmcts.juror.support.sql;

import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.sql.dto.CreateJurorPoolRequest;
import uk.gov.hmcts.juror.support.sql.dto.CreatePopulatedPoolRequest;
import uk.gov.hmcts.juror.support.sql.entity.*;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class SqlDataLoad {


    public static List<JurorPool> createPopulatedPool(CreatePopulatedPoolRequest populatedPoolRequest) {
        final PoolRequest pool = populatedPoolRequest.getPoolRequestGenerator().generate();

        List<JurorPool> jurorPools = new ArrayList<>();


        populatedPoolRequest.getJurorCountMap()
            .forEach((key, value) -> jurorPools.addAll(createJurorPools(pool, key, value)));


        return jurorPools;
    }

    public static List<JurorPool> createJurorPools(PoolRequest pool, CreateJurorPoolRequest jurorPoolRequest, int count) {
        final List<JurorPool> jurorPools = new ArrayList<>(count);

        final JurorPoolGenerator jurorPoolGenerator = jurorPoolRequest.getJurorPoolGenerator();
        jurorPoolGenerator.setPool(new FixedValueGeneratorImpl<>(pool));

        for(int i = 0; i < count; i++) {
            jurorPools.add(createJurorPool(jurorPoolRequest));
        }
        return jurorPools;
    }

    public static JurorPool createJurorPool(CreateJurorPoolRequest jurorPoolRequest) {
        final Juror juror = jurorPoolRequest.getJurorGenerator().generate();
        final JurorPoolGenerator jurorPoolGenerator = jurorPoolRequest.getJurorPoolGenerator();
        jurorPoolGenerator.setJuror(new FixedValueGeneratorImpl<>(juror));

        return jurorPoolGenerator.generate();
    }
}
