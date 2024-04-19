package uk.gov.hmcts.juror.support.sql.v1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.juror.support.generation.generators.code.GenerateGenerationConfig;
import uk.gov.hmcts.juror.support.generation.generators.value.DateFilter;
import uk.gov.hmcts.juror.support.generation.generators.value.DateTimeFilter;
import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalDateTimeGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.TimeFilter;
import uk.gov.hmcts.juror.support.sql.v1.repository.PoolRequestRepository;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "juror_pool", schema = "juror_mod")
@IdClass(JurorPoolId.class)
@NoArgsConstructor
@Getter
@Setter
@ToString
@Slf4j
@GenerateGenerationConfig
public class JurorPool implements Serializable {

    @Id
    @Column(name = "juror_number")
    private String jurorNumber;

    @Id
    @Column(name = "pool_number")
    private String poolNumber;

    //    @NotNull
    @Column(name = "owner")
//    @Length(max = 3)
    private String owner;


    @Getter(AccessLevel.NONE)
    @Transient
    private String locCode;


    public String getLocCode(PoolRequestRepository poolRequestRepository) {
        if (locCode == null) {
            locCode = poolRequestRepository.findById(poolNumber).get().getCourtLocation().getLocCode();
        }
        return locCode;
    }

    @Column(name = "user_edtq")
//    @Length(max = 20)
    private String userEdtq;

    @Column(name = "is_active")
    @FixedValueGenerator("true")
    private Boolean isActive;

    private Integer status;

    @Column(name = "times_sel")
    private Integer timesSelected;

    @Column(name = "def_date")
    private LocalDate deferralDate;

    //    @Length(max = 6)
    @Column(name = "location")
    private String location;

    @Column(name = "no_attendances")
    private Integer noAttendances;

    @Column(name = "no_attended")
    private Integer noAttended;

    @Column(name = "no_fta")
    private Integer failedToAttendCount;

    @Column(name = "no_awol")
    private Integer unauthorisedAbsenceCount;

    //    @Length(max = 4)
    @Column(name = "pool_seq")
    private String poolSequence;

    @Column(name = "edit_tag")
    private Character editTag;

    @Column(name = "next_date")
    private LocalDate nextDate;

    @Column(name = "on_call")
    private Boolean onCall;

    //    @Length(max = 20)
    @Column(name = "smart_card")
    private String smartCard;


    @Column(name = "was_deferred")
    private Boolean wasDeferred;

    //    @Length(max = 1)
    @Column(name = "deferral_code")
    private String deferralCode;

    @Column(name = "id_checked")
    private Character idChecked;

    @Column(name = "postpone")
    private Boolean postpone;

    @Column(name = "paid_cash")
    private Boolean paidCash;

    //    @Length(max = 9)
    @Column(name = "scan_code")
    private String scanCode;


    @Column(name = "reminder_sent")
    private Boolean reminderSent;

    @Column(name = "transfer_date")
    private LocalDate transferDate;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @Column(name = "date_created")
    @LocalDateTimeGenerator(
        minInclusive = @DateTimeFilter(
            dateFilter = @DateFilter(mode = DateFilter.Mode.MINUS, value = 400, unit = ChronoUnit.DAYS),
            timeFilter = @TimeFilter(mode = TimeFilter.Mode.MINUS, value = 10, unit = ChronoUnit.MINUTES)
        ),
        maxExclusive = @DateTimeFilter(
            dateFilter = @DateFilter(mode = DateFilter.Mode.MINUS, value = 0, unit = ChronoUnit.DAYS),
            timeFilter = @TimeFilter(mode = TimeFilter.Mode.PLUS, value = 10, unit = ChronoUnit.MINUTES)
        )
    )
    private LocalDateTime dateCreated;

}
