package uk.gov.hmcts.juror.support.sql.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.juror.support.generation.generators.value.*;
import uk.gov.hmcts.juror.support.generation.generators.value.SequenceGenerator;
import uk.gov.hmcts.juror.support.sql.Constants;
import uk.gov.hmcts.juror.support.sql.entity.jurorresponse.Address;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@MappedSuperclass
@Table(name = "juror_response", schema = "juror_mod")
@Getter
@Setter
public class AbstractJurorResponse extends Address implements Serializable {

    @Id
    @Column(name = "juror_number")
    @StringSequenceGenerator(format = "%09d",
            sequenceGenerator = @SequenceGenerator(id = "juror_number", start = 1)
    )
    private String jurorNumber;

    @Column(name = "date_received")
    @LocalDateGenerator(
            minInclusive = @DateFilter(mode = DateFilter.Mode.MINUS, value = 6, unit = ChronoUnit.MONTHS),
            maxExclusive = @DateFilter(mode = DateFilter.Mode.MINUS, value = 2, unit = ChronoUnit.MONTHS)
    )
    private LocalDate dateReceived;

    @Column(name = "title")
    @RandomFromFileGenerator(file = "data/titles.txt")
    private String title;

    @Column(name = "first_name")
    @FirstNameGenerator
    private String firstName;

    @Column(name = "last_name")
    @LastNameGenerator
    private String lastName;

    @Column(name = "processing_status")
    @FixedValueGenerator("success")
    private String processingStatus;

    @Column(name = "date_of_birth")
    @LocalDateGenerator(
            minInclusive = @DateFilter(mode = DateFilter.Mode.MINUS, value = 75, unit = ChronoUnit.YEARS),
            maxExclusive = @DateFilter(mode = DateFilter.Mode.MINUS, value = 18, unit = ChronoUnit.YEARS)
    )
    private LocalDate dateOfBirth;

    /**
     * Juror phone number.
     */
    @Column(name = "phone_number")
    @RegexGenerator(regex = Constants.PHONE_REGEX)
    private String phoneNumber;

    /**
     * Juror alternative number.
     */
    @Column(name = "alt_phone_number")
    @RegexGenerator(regex = Constants.PHONE_REGEX)
    private String altPhoneNumber;

    /**
     * Juror email address.
     */
    @Column(name = "email")
    @EmailGenerator
    private String email;

    @Column(name = "residency")
    @FixedValueGenerator("true")
    private Boolean residency;

    @Column(name = "mental_health_act")
    @FixedValueGenerator("false")
    private Boolean mentalHealthAct;

    @Column(name = "bail")
    @FixedValueGenerator("false")
    private Boolean bail;

    @Column(name = "convictions")
    @FixedValueGenerator("false")
    private Boolean convictions;

    @Column(name = "thirdparty_reason")
    @NullValueGenerator
    private String thirdPartyReason;

    @Column(name = "reasonable_adjustments_arrangements")
    @NullValueGenerator
    private String reasonableAdjustmentsArrangements;

    @Column(name = "processing_complete")
    @FixedValueGenerator("false")
    private Boolean processingComplete = Boolean.FALSE;

    @Column(name = "completed_at")
    @NullValueGenerator
    private LocalDate completedAt;

    @Column(name = "relationship")
    @NullValueGenerator
    private String relationship;

    //TODO Probably need this later for data integrity
//    @ManyToOne
//    @JoinColumn(name = "staff_login")
//    private User staff;

    /**
     * Flag that this response is urgent.
     */
    @Column(name = "urgent")
    @FixedValueGenerator("false")
    private Boolean urgent;

    /**
     * Flag this response as super urgent.
     */
    @Column(name = "super_urgent")
    @FixedValueGenerator("false")
    private Boolean superUrgent;

    /**
     * Flag this response as welsh language.
     */
    @Column(name = "welsh")
    @FixedValueGenerator("false")
    private Boolean welsh = Boolean.FALSE;


    protected AbstractJurorResponse() {
        // This constructor is intentionally empty. Nothing special is needed here.
    }
}
