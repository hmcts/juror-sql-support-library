package uk.gov.hmcts.juror.support.sql.entity.jurorresponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.juror.support.generation.generators.value.*;
import uk.gov.hmcts.juror.support.sql.Constants;
import uk.gov.hmcts.juror.support.sql.entity.AbstractJurorResponse;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
//@DigitalThirdPartyOtherReason
@Table(name = "juror_response", schema = "juror_mod")
@EqualsAndHashCode(callSuper = true)
public class DigitalResponse extends AbstractJurorResponse {

    @Column(name = "residency")
    @FixedValueGenerator("true")
    private Boolean residency = Boolean.TRUE;

    @Column(name = "bail")
    @FixedValueGenerator("false")
    private Boolean bail = Boolean.FALSE;

    @Column(name = "convictions")
    @FixedValueGenerator("false")
    private Boolean convictions = Boolean.FALSE;

    @Column(name = "mental_health_act")
    @FixedValueGenerator("false")
    private Boolean mentalHealthAct = Boolean.FALSE;

    @Column(name = "residency_detail")
    @FixedValueGenerator("true")
    private String residencyDetail;

    @Column(name = "mental_health_act_details")
    @FixedValueGenerator("false")
    private String mentalHealthActDetails;

    @Column(name = "bail_details")
    @FixedValueGenerator("false")
    private String bailDetails;

    @Column(name = "convictions_details")
    @FixedValueGenerator("false")
    private String convictionsDetails;

    @Column(name = "deferral_reason")
    @FixedValueGenerator("false")
    private String deferralReason;

    @Column(name = "deferral_date")
    @LocalDateGenerator(
            minInclusive = @DateFilter(mode = DateFilter.Mode.PLUS, value = 4, unit = ChronoUnit.MONTHS),
            maxExclusive = @DateFilter(mode = DateFilter.Mode.PLUS, value = 2, unit = ChronoUnit.WEEKS)
    )
    private String deferralDate;

    @Column(name = "excusal_reason")
    @FixedValueGenerator("d")
    private String excusalReason;

    @Version
    @Column(name = "version")
    @FixedValueGenerator("1")
    private Integer version;

    @Column(name = "thirdparty_fname")
    @FirstNameGenerator
    private String thirdPartyFName;

    @Column(name = "thirdparty_lname")
    @LastNameGenerator
    private String thirdPartyLName;

    @Column(name = "main_phone")
    @RegexGenerator(regex = Constants.PHONE_REGEX)
    private String mainPhone;

    @Column(name = "other_phone")
    @RegexGenerator(regex = Constants.PHONE_REGEX)
    private String otherPhone;

    @Column(name = "email_address")
    @EmailGenerator
    private String emailAddress;

    @Column(name = "thirdparty_other_reason")
//    @Pattern(regexp = NO_PIPES_REGEX)
    @FixedValueGenerator("ill")
    private String thirdPartyOtherReason;

    @Column(name = "juror_phone_details")
    @Builder.Default
    @RegexGenerator(regex = Constants.PHONE_REGEX)
    private Boolean jurorPhoneDetails = Boolean.TRUE;

    @Column(name = "juror_email_details")
    @Builder.Default
    @EmailGenerator
    private Boolean jurorEmailDetails = Boolean.TRUE;

    @Column(name = "staff_assignment_date")
    @LocalDateGenerator(
            minInclusive = @DateFilter(mode = DateFilter.Mode.MINUS, value = 5, unit = ChronoUnit.MONTHS),
            maxExclusive = @DateFilter(mode = DateFilter.Mode.MINUS, value = 12, unit = ChronoUnit.DAYS)
    )
    private LocalDate staffAssignmentDate;

    public DigitalResponse() {
        super();
    }
}
