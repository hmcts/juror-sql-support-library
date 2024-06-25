package uk.gov.hmcts.juror.support.sql.v2.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.AppearanceStage;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.AttendanceType;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.FoodDrinkClaimType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appearance", schema = "juror_mod")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AppearanceId.class)
@Builder
@ToString
@Getter
@Setter
@SuppressWarnings({"PMD.TooManyFields", "PMD.LawOfDemeter", "PMD.TooManyImports"})
public class Appearance implements Serializable {

    @Version
    private Long version;

    @Id
    @Column(name = "juror_number")
//    @Pattern(regexp = JUROR_NUMBER)
//    @Length(max = 9)
    private String jurorNumber;

    @Id
    @NotNull
    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    @Id
    @NotNull
    @Column(name = "loc_code")
    private String locCode;


    //    @Length(max = 9)
    @Column(name = "pool_number")
    private String poolNumber;

    /**
     * Flag recording whether the juror sat on a jury (true = sat on jury).
     */
    @Column(name = "sat_on_jury")
    private Boolean satOnJury;

    @Column(name = "time_in")
    private LocalTime timeIn;

    @Column(name = "time_out")
    private LocalTime timeOut;

    @Column(name = "trial_number")
//    @Length(max = 16)
    private String trialNumber;

    @Column(name = "appearance_stage")
    @Enumerated(EnumType.STRING)
    private AppearanceStage appearanceStage;

    @Column(name = "attendance_type")
    @Enumerated(EnumType.STRING)
    private AttendanceType attendanceType;

    @Column(name = "non_attendance")
    @Builder.Default
    private Boolean nonAttendanceDay = false;

    @Column(name = "misc_description")
    private String miscDescription;

    @Column(name = "pay_cash")
    @Builder.Default
    private boolean payCash = false;

    @Column(name = "last_updated_by")
    private String lastUpdatedBy;

    @Column(name = "created_by")
    private String createdBy;

    // transport expenses
    @Column(name = "public_transport_total_due")
    private BigDecimal publicTransportDue;
    @Column(name = "public_transport_total_paid")
    private BigDecimal publicTransportPaid;

    @Column(name = "hired_vehicle_total_due")
    private BigDecimal hiredVehicleDue;
    @Column(name = "hired_vehicle_total_paid")
    private BigDecimal hiredVehiclePaid;

    @Column(name = "motorcycle_total_due")
    private BigDecimal motorcycleDue;
    @Column(name = "motorcycle_total_paid")
    private BigDecimal motorcyclePaid;

    @Column(name = "car_total_due")
    private BigDecimal carDue;
    @Column(name = "car_total_paid")
    private BigDecimal carPaid;

    @Column(name = "pedal_cycle_total_due")
    private BigDecimal bicycleDue;
    @Column(name = "pedal_cycle_total_paid")
    private BigDecimal bicyclePaid;

    @Column(name = "parking_total_due")
    private BigDecimal parkingDue;
    @Column(name = "parking_total_paid")
    private BigDecimal parkingPaid;

    // childcare expenses
    @Column(name = "childcare_total_due")
    private BigDecimal childcareDue;
    @Column(name = "childcare_total_paid")
    private BigDecimal childcarePaid;

    // miscellaneous expenses
    @Column(name = "misc_total_due")
    private BigDecimal miscAmountDue;
    @Column(name = "misc_total_paid")
    private BigDecimal miscAmountPaid;

    // Loss of earnings (different caps apply for half day/full day/long trials)
    @Column(name = "loss_of_earnings_due")
    private BigDecimal lossOfEarningsDue;
    @Column(name = "loss_of_earnings_paid")
    private BigDecimal lossOfEarningsPaid;

    // Subsistence (food and drink) expenses (same flat rate applies for whole day/half day/long day/overnight).
    @Column(name = "subsistence_due")
    private BigDecimal subsistenceDue;
    @Column(name = "subsistence_paid")
    private BigDecimal subsistencePaid;

    /**
     * Amount spent on a smart card (credit), usually on food and drink in a canteen (to be deducted from expenses due).
     */
    @Column(name = "smart_card_due")
    private BigDecimal smartCardAmountDue;

    /**
     * Amount of spend on a smart card that has been deducted prior to payment.
     */
    @Column(name = "smart_card_paid")
    private BigDecimal smartCardAmountPaid;


    @Column(name = "travel_time")
    private LocalTime travelTime;

    @Column(name = "travel_by_car")
    private Boolean traveledByCar;

    @Column(name = "travel_jurors_taken_by_car")
    @Min(0)
    private Integer jurorsTakenCar;

    @Column(name = "travel_by_motorcycle")
    private Boolean traveledByMotorcycle;

    @Column(name = "travel_jurors_taken_by_motorcycle")
    @Min(0)
    private Integer jurorsTakenMotorcycle;

    @Column(name = "travel_by_bicycle")
    private Boolean traveledByBicycle;


    @Column(name = "miles_traveled")
    private Integer milesTraveled;

    @Column(name = "food_and_drink_claim_type")
    @Enumerated(EnumType.STRING)
    private FoodDrinkClaimType foodAndDrinkClaimType;

    /**
     * Flag indicating whether the expense is being "saved for later" in a draft state (true) or if it is ready to be
     * authorised (false).
     */
    @Column(name = "is_draft_expense")
    @Builder.Default
    private boolean isDraftExpense = true;

    /**
     * flag indicating whether the juror has not attended court on a day they were due to be present (unauthorised
     * absence).
     */
    @Column(name = "no_show")
    private Boolean noShow;


    /**
     * Sequence generated number to group jurors by when their attendance is confirmed. Uses either a 'P' prefix for
     * pool attendance (Jurors in waiting) or a 'J' prefix for jury attendance (serving on jury for a trial)
     */
    @Column(name = "attendance_audit_number")
    private String attendanceAuditNumber;
}
