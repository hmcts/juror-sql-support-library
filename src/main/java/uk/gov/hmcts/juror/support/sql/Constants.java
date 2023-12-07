package uk.gov.hmcts.juror.support.sql;

import uk.gov.hmcts.juror.support.sql.generators.PoolRequestGeneratorUtil;

import java.util.Map;

public class Constants {

    public static final String PHONE_REGEX = "^(\\+44|0)7\\d{9}$";
    public static final String NOTES_REGEX = "[A-Za-z0-9]{10,501}";

    public static final Map<PoolRequestGeneratorUtil.CourtType, Integer> POOL_REQUEST_WEIGHT_MAP;

    static {
        POOL_REQUEST_WEIGHT_MAP = Map.of(
            PoolRequestGeneratorUtil.CourtType.CIVIL, 145,
            PoolRequestGeneratorUtil.CourtType.CRIMINAL, 4,
            PoolRequestGeneratorUtil.CourtType.CROWN, 62_156,
            PoolRequestGeneratorUtil.CourtType.HIGH, 15,
            PoolRequestGeneratorUtil.CourtType.CORONER, 1
        );
    }
}
