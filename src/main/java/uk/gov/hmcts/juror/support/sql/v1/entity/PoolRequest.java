package uk.gov.hmcts.juror.support.sql.v1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.gov.hmcts.juror.support.generation.generators.code.GenerateGenerationConfig;
import uk.gov.hmcts.juror.support.generation.generators.value.DateFilter;
import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.IntRangeGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalDateGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.SequenceGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.StringSequenceGenerator;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "pool", schema = "juror_mod")
@NoArgsConstructor
@Getter
@Setter
@ToString
@GenerateGenerationConfig
public class PoolRequest implements Serializable {

    @Id
//    @NotNull
    @Column(name = "pool_no")
//    @Length(max = 9)
    @StringSequenceGenerator(format = "%09d",
        sequenceGenerator = @SequenceGenerator(id = "pool_number", start = 1)
    )
    private String poolNumber;


    //    @NotNull
    @Column(name = "owner")
//    @Length(max = 3)
    private String owner;

    @ManyToOne
    @JoinColumn(name = "loc_code", nullable = false)
    private CourtLocation courtLocation;

    //    @NotNull
    @Column(name = "return_date")
    @LocalDateGenerator(
        minInclusive = @DateFilter(mode = DateFilter.Mode.PLUS, value = 7, unit = ChronoUnit.DAYS),
        maxExclusive = @DateFilter(mode = DateFilter.Mode.PLUS, value = 9, unit = ChronoUnit.WEEKS)
        //TODO Confirm dates with chris
    )
    private LocalDate returnDate;

    @Column(name = "no_requested")
    @IntRangeGenerator(minInclusive = 20, maxExclusive = 50)
    private Integer numberRequested;

    private String poolType;

    @Column(name = "attend_time")
    //TODO
//    @LocalDateTimeGenerator(
//        minInclusive = @DateTimeFilter(),
//        maxExclusive = @DateTimeFilter()
//    )
    private LocalDateTime attendTime;

    @Column(name = "new_request")
    @FixedValueGenerator("'N'")
    private Character newRequest;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @Column(name = "additional_summons")
    private Integer additionalSummons;

    @Column(name = "total_no_required")
    private int totalNoRequired;

    @Column(name = "nil_pool")
    private boolean nilPool;

    @Column(name = "date_created", updatable = false)
    private LocalDateTime dateCreated;
}
