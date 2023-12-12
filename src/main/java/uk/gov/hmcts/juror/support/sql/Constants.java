package uk.gov.hmcts.juror.support.sql;

import uk.gov.hmcts.juror.support.sql.generators.PoolRequestGeneratorUtil;

import java.util.Map;

public class Constants {

    public static final String PHONE_REGEX = "^(\\+44|0)7\\d{9}$";
    public static final String NOTES_REGEX = "[A-Za-z0-9]{10,501}";
    public static final String ADDRESS_LINE_1_REGEX = "[0-9]{0,9} [A-Z]{5,15} (Road|Lane|Gate|Close|Avenue|Street|Way|Drive|Gardens|Crescent|"
            + "Terrace|Place|Hill|Park|View|Court|Square|Walk|Lane|Grove|Gardens|Hill|Hillside|Hilltop|Hollow|"
            + "House|Housing|Hurst|Industrial|Ings|Island|Islands|Isle|Isles|Junction|Keep|Kings|Knapp|Knoll|"
            + "Knolls|Lair|Lake|Lakes|Landing|Lane|Lanes|Lawn|Lawns|Lea|Leas|Lee|Lees|Line|Link|Little|Lodge|"
            + "Loft|Loggia|Long|Lowlands|Main|Manor|Mansion|Manse|Mead|Meadow|Meadows|Meander|Mews|Mill|Mission|"
            + "Moor|Moorings|Moorland|Mount|Mountain|Mountains|Mound|Mounts|Neuk|Nook|Orchard|Oval|Overlook|"
            + "Park|Parklands|Parkway|Parade|Paradise|Parc|Parks|Part|Passage|Path|Pathway|Pike|Pines|Place|"
            + "Plain|Plains|Plaza|Point|Points|Port|Ports|Prairie|Quadrant|Quay|Quays|Ramble|Ramp|Ranch|Rapids|"
            + "Reach|Reserve|Rest|Retreat|Ridge|Ridges|Rise|River|Rivers|Road|Roads|Route|Row|Rue|Run|Shoal|"
            + "Shoals|Shore|Shores|Skyline|Slope|Slopes|Spur|Square|Squares|Stairs|Stead|Strand|Stream|Street|"
            + "Streets|Summit|Terrace|Throughway|Trace|Track|Trafficway|Trail|Trailer|Tunnel|Turnpike|Underpass|"
            + "Union|Unions|Valley|Valleys|Viaduct|View|Views|Village|Villages|Ville|Vista|Walk|Wall|Way|Ways|";
    public static final String ADDRESS_LINE_2_REGEX = "(Apartment|Suite|Room|Floor|Box) Number [0-9]{1,3}";

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
