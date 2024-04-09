package uk.gov.hmcts.juror.support.sql.v2.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.juror.support.generation.generators.code.GenerateGenerationConfig;
import uk.gov.hmcts.juror.support.generation.generators.value.DateFilter;
import uk.gov.hmcts.juror.support.generation.generators.value.FirstNameGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.LastNameGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalDateGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.NullValueGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromFileGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.RegexGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.SequenceGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.StringSequenceGenerator;
import uk.gov.hmcts.juror.support.sql.v1.Constants;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Entity
@IdClass(VotersV2.VotersId.class)
@Table(name = "voters", schema = "juror_mod")
@NoArgsConstructor
@Getter
@Setter
@GenerateGenerationConfig
public class VotersV2 implements Serializable {

    @Id
    @Column(name = "LOC_CODE")
//    @Length(max = 3)
    private String locCode;

    @Id
    @Column(name = "PART_NO")
//    @Pattern(regexp = JUROR_NUMBER)
//    @Length(max = 9)
//    @NotNull
    @StringSequenceGenerator(format = "%09d",
        sequenceGenerator = @SequenceGenerator(id = "juror_number", start = 200000000)
    )
    private String jurorNumber;

    @Column(name = "REGISTER_LETT")
//    @Length(max = 5)
    private String registerLett;

    @Column(name = "POLL_NUMBER")
//    @Length(max = 5)
    private String pollNumber;

    @Column(name = "NEW_MARKER")
//    @Length(max = 1)
    private String newMarker;

    @Column(name = "TITLE")
//    @Length(max = 10)
//    @Pattern(regexp = NO_PIPES_REGEX)
    @RandomFromFileGenerator(file = "data/titles.txt")
    private String title;

    @Column(name = "FNAME")
//    @Length(max = 20)
//    @Pattern(regexp = NO_PIPES_REGEX)
//    @NotNull
    @FirstNameGenerator
    private String firstName;

    @Column(name = "LNAME")
//    @Length(max = 20)
//    @Pattern(regexp = NO_PIPES_REGEX)
//    @NotNull
    @LastNameGenerator
    private String lastName;

    @Column(name = "DOB")
//    @LocalDateOfBirth
//    @LocalDateGenerator(
//        minInclusive = @DateFilter(mode = DateFilter.Mode.MINUS, value = 75, unit = ChronoUnit.YEARS),
//        maxExclusive = @DateFilter(mode = DateFilter.Mode.MINUS, value = 18, unit = ChronoUnit.YEARS)
//    )
    private LocalDate dateOfBirth;

    @Column(name = "ADDRESS")
//    @Length(max = 35)
//    @Pattern(regexp = NO_PIPES_REGEX)
//    @NotNull
    @RegexGenerator(regex = Constants.ADDRESS_LINE_1_REGEX)
    private String address;

    @Column(name = "ADDRESS2")
//    @Length(max = 35)
//    @Pattern(regexp = NO_PIPES_REGEX)
    @RegexGenerator(regex = Constants.ADDRESS_LINE_2_REGEX)
    private String address2;

    @Column(name = "ADDRESS3")
//    @Length(max = 35)
//    @Pattern(regexp = NO_PIPES_REGEX)
    @RandomFromFileGenerator(file = "data/city.txt")
    private String address3;

    @Column(name = "ADDRESS4")
//    @Length(max = 35)
//    @Pattern(regexp = NO_PIPES_REGEX)
    @RandomFromFileGenerator(file = "data/county.txt")
    private String address4;

    @Column(name = "ADDRESS5")
//    @Length(max = 35)
//    @Pattern(regexp = NO_PIPES_REGEX)
    @NullValueGenerator
    private String address5;

    @Column(name = "ADDRESS6")
//    @Null
    @NullValueGenerator
    private String address6 = null;

    @Column(name = "ZIP")
//    @Pattern(regexp = POSTCODE_REGEX)
//    @Length(max = 10)
    @RandomFromFileGenerator(file = "data/postcode.txt")
    private String postcode;

    @Column(name = "DATE_SELECTED1")
    private Date dateSelected1;

    @Column(name = "REC_NUM")
    private Integer recNumber;

    //    @Size(max = 1)
    @Column(name = "PERM_DISQUAL")
    private String permDisqual;

    //    @Size(max = 1)
    @Column(name = "FLAGS")
    private String flags;

    @Column(name = "SOURCE_ID")
//    @Length(max = 1)
    private String sourceId;

    @EqualsAndHashCode
    @Setter
    @Getter
    public static class VotersId implements Serializable {
        private String locCode;
        private String jurorNumber;
    }
}
