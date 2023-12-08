package uk.gov.hmcts.juror.support.sql.entity.jurorresponse;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.juror.support.generation.generators.value.NullValueGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromFileGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.RegexGenerator;

import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode
public abstract class Address implements Serializable {

    @Column(name = "address_line_1")
    @NotBlank
    @RegexGenerator(regex = "[0-9]{0,9} [A-Z]{5,15} (Road|Lane|Gate|Close|Avenue|Street|Way|Drive|Gardens|Crescent|"
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
            + "Union|Unions|Valley|Valleys|Viaduct|View|Views|Village|Villages|Ville|Vista|Walk|Wall|Way|Ways|")
    private String addressLine1;

    @Column(name = "address_line_2")
    @RegexGenerator(regex = "(Apartment|Suite|Room|Floor|Box) Number [0-9]{1,3}")
    private String addressLine2;

    @Column(name = "address_line_3")
    @RandomFromFileGenerator(file = "data/city.txt")
    private String addressLine3;

    @Column(name = "address_line_4")
    @RandomFromFileGenerator(file = "data/county.txt")
    private String addressLine4;

    @Column(name = "address_line_5")
    @NullValueGenerator
    private String addressLine5;

    @Column(name = "postcode")
    @RandomFromFileGenerator(file = "data/postcode.txt")
    private String postcode;

}

