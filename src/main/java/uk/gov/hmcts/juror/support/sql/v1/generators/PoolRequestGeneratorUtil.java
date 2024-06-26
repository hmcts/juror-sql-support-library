package uk.gov.hmcts.juror.support.sql.v1.generators;

import lombok.Getter;
import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.sql.v1.Util;
import uk.gov.hmcts.juror.support.sql.v1.entity.CourtLocation;
import uk.gov.hmcts.juror.support.sql.v1.entity.PoolRequestGenerator;
import uk.gov.hmcts.juror.support.sql.v1.service.SqlSupportService;

import java.util.EnumMap;
import java.util.Map;

public class PoolRequestGeneratorUtil {
    private static RandomFromCollectionGeneratorImpl<CourtLocation> courtLocationGenerator;


    public static PoolRequestGenerator crownCourt(boolean isCourtOwned) {
        return createBaseGenerator(isCourtOwned, CourtType.CROWN);
    }

    public static PoolRequestGenerator coronerCourt(boolean isCourtOwned) {
        return createBaseGenerator(isCourtOwned, CourtType.CORONER);
    }


    public static PoolRequestGenerator criminalCourt(boolean isCourtOwned) {
        return createBaseGenerator(isCourtOwned, CourtType.CRIMINAL);
    }

    public static PoolRequestGenerator civilCourt(boolean isCourtOwned) {
        return createBaseGenerator(isCourtOwned, CourtType.CIVIL);
    }

    public static PoolRequestGenerator highCourt(boolean isCourtOwned) {
        return createBaseGenerator(isCourtOwned, CourtType.HIGH);
    }

    private static PoolRequestGenerator createBaseGenerator(boolean isCourtOwned, CourtType courtType) {
        PoolRequestGenerator poolRequestGenerator = new PoolRequestGenerator();
        poolRequestGenerator.setCourtLocation(getCourtLocationGenerator());
        poolRequestGenerator.setPoolType(new FixedValueGeneratorImpl<>(courtType.getCode()));
        if (isCourtOwned) {
            poolRequestGenerator.addPostGenerate(poolRequest -> {
                    poolRequest.setOwner(poolRequest.getCourtLocation().getOwner());
                }
            );
            poolRequestGenerator.setNewRequest(new FixedValueGeneratorImpl<>('T'));
        } else {
            poolRequestGenerator.addPostGenerate(poolRequest -> {
                poolRequest.setOwner("400");
            });
        }
        poolRequestGenerator.addPostGenerate(poolRequest -> {
            CourtLocation courtLocation = poolRequest.getCourtLocation();
            poolRequest.setPoolNumber(courtLocation.getLocCode() + poolRequest.getPoolNumber().substring(3));
        });
        return poolRequestGenerator;
    }

    private static RandomFromCollectionGeneratorImpl<CourtLocation> getCourtLocationGenerator() {
        if (courtLocationGenerator == null) {
            courtLocationGenerator = new RandomFromCollectionGeneratorImpl<>(SqlSupportService.getCourtLocations());
        }
        return courtLocationGenerator;
    }

    public static PoolRequestGenerator create(boolean isCourtOwned) {
        return create(isCourtOwned, new EnumMap<>(CourtType.class));
    }

    public static PoolRequestGenerator create(boolean isCourtOwned, Map<CourtType, Integer> courtTypeByWight) {
        CourtType courtType = Util.getWeightedRandomItem(courtTypeByWight);
        return create(isCourtOwned, courtType);
    }

    public static PoolRequestGenerator create(boolean isCourtOwned, CourtType courtType) {
        return switch (courtType) {
            case CROWN -> crownCourt(isCourtOwned);
            case CORONER -> coronerCourt(isCourtOwned);
            case CRIMINAL -> criminalCourt(isCourtOwned);
            case CIVIL -> civilCourt(isCourtOwned);
            case HIGH -> highCourt(isCourtOwned);
        };
    }

    @Getter
    public enum CourtType {
        CROWN("CRO"),
        CORONER("COR"),
        CRIMINAL("CRI"),
        CIVIL("CIV"),
        HIGH("HGH");

        private final String code;

        CourtType(String code) {
            this.code = code;
        }
    }
}
