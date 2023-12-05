package uk.gov.hmcts.juror.support.sql;

import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.sql.dto.CreateJurorPoolRequest;
import uk.gov.hmcts.juror.support.sql.dto.CreatePopulatedPoolRequest;
import uk.gov.hmcts.juror.support.sql.entity.Juror;
import uk.gov.hmcts.juror.support.sql.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.entity.JurorPoolGenerator;
import uk.gov.hmcts.juror.support.sql.entity.Pool;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class SqlDataLoad {


    public static List<JurorPool> createPopulatedPool(CreatePopulatedPoolRequest populatedPoolRequest) {
        final Pool pool = populatedPoolRequest.getPoolGenerator().generate();

        List<JurorPool> jurorPools = new ArrayList<>();


        populatedPoolRequest.getJurorCountMap()
            .forEach((key, value) -> jurorPools.addAll(createJurorPools(pool, key, value)));


        return jurorPools;
    }

    public static List<JurorPool> createJurorPools(Pool pool, CreateJurorPoolRequest jurorPoolRequest, int count) {
        final List<JurorPool> jurorPools = new ArrayList<>(count);

        final JurorPoolGenerator jurorPoolGenerator = jurorPoolRequest.getJurorPoolGenerator();
        jurorPoolGenerator.setPoolNumber(new FixedValueGeneratorImpl<>(pool.getPoolNumber()));

        for(int i = 0; i < count; i++) {
            jurorPools.add(createJurorPool(jurorPoolRequest));
        }
        return jurorPools;
    }

    public static JurorPool createJurorPool(CreateJurorPoolRequest jurorPoolRequest) {
        final Juror juror = jurorPoolRequest.getJurorGenerator().generate();
        final JurorPoolGenerator jurorPoolGenerator = jurorPoolRequest.getJurorPoolGenerator();
        jurorPoolGenerator.setJurorNumber(new FixedValueGeneratorImpl<>(juror.getJurorNumber()));

        return jurorPoolGenerator.generate();
    }
}
