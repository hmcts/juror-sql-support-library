package uk.gov.hmcts.juror.support.sql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.juror.support.generation.generators.code.GenerateGenerationConfig;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

//@Entity
//@Table(name = "juror_pool", schema = "juror_mod")
@IdClass(JurorPoolId.class)
@NoArgsConstructor
@Getter
@Setter
@Slf4j
@GenerateGenerationConfig
public class JurorPool implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "juror_number", nullable = false)
    private Juror juror;

    @Id
    @ManyToOne
    @JoinColumn(name = "pool_number", nullable = false)
    private PoolRequest pool;

//    @NotNull
    @Column(name = "owner")
//    @Length(max = 3)
    private String owner;

    /**
     * The date a juror is expected to first attend court for jury service.
     */
    @Column(name = "ret_date")
    private LocalDate startDate;

    @Column(name = "user_edtq")
//    @Length(max = 20)
    private String userEdtq;

    @Column(name = "is_active")
    private Boolean isActive;

    private Integer status;

    @Column(name = "times_sel")
    private Integer timesSelected;

    @Column(name = "def_date")
    private LocalDate deferralDate;

    @Column(name = "mileage")
    private Integer mileage;

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

    @Column(name = "amt_spent", precision = 8)
    private Double amountSpent;

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

    @Column(name = "travel_time", precision = 5)
    private Double travelTime;

//    @Length(max = 9)
    @Column(name = "scan_code")
    private String scanCode;

    @Column(name = "financial_loss", precision = 8)
    private Double financialLoss;

    @Column(name = "reminder_sent")
    private Boolean reminderSent;

    @Column(name = "transfer_date")
    private LocalDate transferDate;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;


}
