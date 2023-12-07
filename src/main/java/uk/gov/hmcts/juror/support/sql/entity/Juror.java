package uk.gov.hmcts.juror.support.sql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.gov.hmcts.juror.support.generation.generators.code.GenerateGenerationConfig;
import uk.gov.hmcts.juror.support.generation.generators.value.DateFilter;
import uk.gov.hmcts.juror.support.generation.generators.value.EmailGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.FirstNameGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.LastNameGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalDateGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.NullValueGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromFileGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.RegexGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.SequenceGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.StringSequenceGenerator;
import uk.gov.hmcts.juror.support.sql.Constants;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "juror", schema = "juror_mod")
@NoArgsConstructor
@Getter
@Setter
@ToString
@GenerateGenerationConfig
public class Juror implements Serializable {

    @Id
    ////@Audited
    ////@NotBlank
    @Column(name = "juror_number")
    ////@Pattern(regexp = JUROR_NUMBER)
    ////@Length(max = 9)
    @StringSequenceGenerator(format = "%09d",
        sequenceGenerator = @SequenceGenerator(id = "juror_number", start = 1)
    )
    private String jurorNumber;

    @Column(name = "poll_number")
    //@Length(max = 5)
    private String pollNumber;

    //@Audited
    @Column(name = "title")
    //@Length(max = 10)
    //@Pattern(regexp = NO_PIPES_REGEX)
    @RandomFromFileGenerator(file = "data/titles.txt")
    private String title;

    //@Audited
    @Column(name = "first_name")
    //@Length(max = 20)
    //@Pattern(regexp = NO_PIPES_REGEX)
    //@NotBlank
    @FirstNameGenerator
    private String firstName;

    //@Audited
    @Column(name = "last_name")
    //@Length(max = 20)
    //@Pattern(regexp = NO_PIPES_REGEX)
    //@NotBlank
    @LastNameGenerator
    private String lastName;

    //@Audited
    @Column(name = "dob")
    @LocalDateGenerator(
        minInclusive = @DateFilter(mode = DateFilter.Mode.MINUS, value = 75, unit = ChronoUnit.YEARS),
        maxExclusive = @DateFilter(mode = DateFilter.Mode.MINUS, value = 18, unit = ChronoUnit.YEARS)
    )
    private LocalDate dateOfBirth;

    //@Audited
    @Column(name = "address_line_1")
    //@Length(max = 35)
    //@Pattern(regexp = NO_PIPES_REGEX)
    //@NotBlank
    @RegexGenerator(regex = "[0-9]{0,3} [A-Z]{5,10} (Road|Lane|Gate|Close|Avenue|Street|Way|Drive|Gardens|Crescent|"
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

    //@Audited
    @Column(name = "address_line_2")
    //@Length(max = 35)
    //@Pattern(regexp = NO_PIPES_REGEX)
    @RegexGenerator(regex = "(Apartment|Suite|Room|Floor|Box) Number [0-9]{1,3}")
    private String addressLine2;

    //@Audited
    @Column(name = "address_line_3")
    //@Length(max = 35)
    //@Pattern(regexp = NO_PIPES_REGEX)
    @RandomFromFileGenerator(file = "data/city.txt")
    private String addressLine3;

    //@Audited
    @Column(name = "address_line_4")
    //@Length(max = 35)
    //@Pattern(regexp = NO_PIPES_REGEX)
    @RandomFromFileGenerator(file = "data/county.txt")
    private String addressLine4;

    //@Audited
    @Column(name = "address_line_5")
    //@Length(max = 35)
    //@Pattern(regexp = NO_PIPES_REGEX)
    @NullValueGenerator
    private String addressLine5;

    //@Audited
    @Column(name = "postcode")
    //@Length(max = 10)
    //@Pattern(regexp = POSTCODE_REGEX)
    @RandomFromFileGenerator(file = "data/postcode.txt")
    private String postcode;

    //@Audited
    @Column(name = "h_phone")
    //@Length(max = 15)
    @RegexGenerator(regex = Constants.PHONE_REGEX)
    private String phoneNumber;

    //@Audited
    @Column(name = "w_phone")
    //@Length(max = 15)
    @RegexGenerator(regex = Constants.PHONE_REGEX)
    private String workPhone;

    //@Audited
    @Column(name = "w_ph_local")
    //@Length(max = 4)
    private String workPhoneExtension;

    //@NotNull
    @Column(name = "responded")
    @FixedValueGenerator(value = "true")
    private boolean responded;

    @Column(name = "date_excused")
    private LocalDate excusalDate;

    //@Length(max = 1)
    @Column(name = "excusal_code")
    private String excusalCode;

    @Column(name = "acc_exc")
    private String excusalRejected;

    @Column(name = "date_disq")
    private LocalDate disqualifyDate;

    //@Length(max = 1)
    @Column(name = "disq_code")
    private String disqualifyCode;

    @Column(name = "user_edtq")
    //@Length(max = 20)
    private String userEdtq;

    //@Length(max = 2000)
    @Column(name = "notes")
    @RegexGenerator(regex = Constants.NOTES_REGEX)
    private String notes;

    @Column(name = "no_def_pos")
    private Integer noDefPos;

    @Column(name = "perm_disqual")
    private Boolean permanentlyDisqualify;

    //@Length(max = 1)
    @Column(name = "reasonable_adj_code")
    private String reasonableAdjustmentCode;

    //@Length(max = 60)
    @Column(name = "reasonable_adj_msg")
    private String reasonableAdjustmentMessage;

    //@Length(max = 20)
    @Column(name = "smart_card")
    private String smartCard;

    @Column(name = "completion_date")
    private LocalDate completionDate;

    //@Audited
    @Column(name = "sort_code")
    //@Length(max = 6)
    private String sortCode;

    //@Audited
    @Column(name = "bank_acct_name")
    //@Length(max = 18)
    private String bankAccountName;

    //@Audited
    @Column(name = "bank_acct_no")
    //@Length(max = 8)
    private String bankAccountNumber;

    //@Audited
    @Column(name = "bldg_soc_roll_no")
    //@Length(max = 18)
    private String buildingSocietyRollNumber;

    @Column(name = "welsh")
    private Boolean welsh;

    @Column(name = "police_check")
    @Enumerated(EnumType.STRING)
    private PoliceCheck policeCheck;

    //@Length(max = 20)
    @Column(name = "summons_file")
    private String summonsFile;

    //@Audited
    @Column(name = "m_phone")
    //@Length(max = 15)
    private String altPhoneNumber;

    //@Audited
    //@Length(max = 254)
    @Column(name = "h_email")
    @EmailGenerator
    private String email;

    @Column(name = "contact_preference")
    private Integer contactPreference;

    @Column(name = "notifications")
    private int notifications;

    //@LastModifiedDate
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    //@CreatedDate
    @Column(name = "date_created", updatable = false)
    private LocalDateTime dateCreated;

    @Column(name = "optic_reference")
    //@Length(max = 8)
    private String opticRef;

    //@Audited
    @Column(name = "pending_title")
    //@Length(max = 10)
    //@Pattern(regexp = NO_PIPES_REGEX)
    private String pendingTitle;

    //@Audited
    @Column(name = "pending_first_name")
    //@Length(max = 20)
    //@Pattern(regexp = NO_PIPES_REGEX)
    private String pendingFirstName;

    //@Audited
    @Column(name = "pending_last_name")
    //@Length(max = 20)
    //@Pattern(regexp = NO_PIPES_REGEX)
    private String pendingLastName;
}
