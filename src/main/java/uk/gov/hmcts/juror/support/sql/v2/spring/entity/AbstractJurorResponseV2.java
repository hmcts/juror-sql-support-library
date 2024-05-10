package uk.gov.hmcts.juror.support.sql.v2.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
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
import uk.gov.hmcts.juror.support.sql.v1.Constants;
import uk.gov.hmcts.juror.support.sql.v1.entity.ProcessingStatus;
import uk.gov.hmcts.juror.support.sql.v1.entity.User;
import uk.gov.hmcts.juror.support.sql.v1.entity.jurorresponse.Address;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Table(name = "juror_response", schema = "juror_mod")
@Getter
@Setter
@Entity
public class AbstractJurorResponseV2 implements Serializable {

    @Id
    @Column(name = "juror_number")
    @StringSequenceGenerator(format = "%09d",
        sequenceGenerator = @SequenceGenerator(id = "juror_number", start = 1)
    )
    private String jurorNumber;

    @Column(name = "date_received")
    @LocalDateGenerator(
        minInclusive = @DateFilter(mode = DateFilter.Mode.MINUS, value = 30, unit = ChronoUnit.DAYS),
        maxExclusive = @DateFilter(mode = DateFilter.Mode.MINUS, value = 0, unit = ChronoUnit.DAYS)
    )
    private LocalDate dateReceived;

    @Column(name = "processing_status")
    @Enumerated(EnumType.STRING)
    @FixedValueGenerator("uk.gov.hmcts.juror.support.sql.v1.entity.ProcessingStatus.TODO")
    private ProcessingStatus processingStatus = ProcessingStatus.TODO;


    @Column(name = "processing_complete")
    @FixedValueGenerator("false")
    private Boolean processingComplete = Boolean.FALSE;

    @Column(name = "completed_at")
    @NullValueGenerator
    private LocalDate completedAt;


    private String replyType;

    protected AbstractJurorResponseV2() {
        // This constructor is intentionally empty. Nothing special is needed here.
    }
}
