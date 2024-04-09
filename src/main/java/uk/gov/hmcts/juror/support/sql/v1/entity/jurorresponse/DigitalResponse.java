package uk.gov.hmcts.juror.support.sql.v1.entity.jurorresponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.juror.support.generation.generators.code.GenerateGenerationConfig;
import uk.gov.hmcts.juror.support.generation.generators.value.DateFilter;
import uk.gov.hmcts.juror.support.generation.generators.value.EmailGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalDateGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.NullValueGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.RegexGenerator;
import uk.gov.hmcts.juror.support.sql.v1.Constants;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
@Table(name = "juror_response", schema = "juror_mod")
@EqualsAndHashCode(callSuper = true)
@GenerateGenerationConfig
public class DigitalResponse extends AbstractJurorResponse {

    @Column(name = "residency")
    @FixedValueGenerator("true")
    private Boolean residency;

    @Column(name = "bail")
    @FixedValueGenerator("false")
    private Boolean bail;

    @Column(name = "convictions")
    @FixedValueGenerator("false")
    private Boolean convictions;

    @Column(name = "mental_health_act")
    @FixedValueGenerator("false")
    private Boolean mentalHealthAct;

    @Column(name = "residency_detail")
    @NullValueGenerator
    private String residencyDetail;

    @Column(name = "mental_health_act_details")
    @NullValueGenerator
    private String mentalHealthActDetails;

    @Column(name = "bail_details")
    @NullValueGenerator
    private String bailDetails;

    @Column(name = "convictions_details")
    @NullValueGenerator
    private String convictionsDetails;

    @Column(name = "deferral_reason")
    @NullValueGenerator
    private String deferralReason;

    @Column(name = "deferral_date")
    @NullValueGenerator
    private String deferralDate;

    @Column(name = "excusal_reason")
    @NullValueGenerator
    private String excusalReason;

    @Version
    @Column(name = "version")
    @NullValueGenerator()
    private Integer version;

    @Column(name = "thirdparty_fname")
    @NullValueGenerator
    private String thirdPartyFName;

    @Column(name = "thirdparty_lname")
    @NullValueGenerator
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
    @NullValueGenerator
    private String thirdPartyOtherReason;

    @Column(name = "juror_phone_details")
    @FixedValueGenerator("true")
    private Boolean jurorPhoneDetails;

    @Column(name = "juror_email_details")
    @FixedValueGenerator("true")
    private Boolean jurorEmailDetails;

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
