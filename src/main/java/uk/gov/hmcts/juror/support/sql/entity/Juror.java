package uk.gov.hmcts.juror.support.sql.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.gov.hmcts.juror.support.generation.generators.code.GenerateGenerationConfig;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "juror", schema = "juror_mod")
@NoArgsConstructor
@Getter
@Setter
@ToString
@GenerateGenerationConfig
public class Juror implements Serializable {

    @Id
    //@Audited
    //@NotBlank
    //@Column(name = "juror_number")
    //@Pattern(regexp = JUROR_NUMBER)
    //@Length(max = 9)
    private String jurorNumber;

    //@Column(name = "poll_number")
    //@Length(max = 5)
    private String pollNumber;

    //@Audited
    //@Column(name = "title")
    //@Length(max = 10)
    //@Pattern(regexp = NO_PIPES_REGEX)
    private String title;

    //@Audited
    //@Column(name = "first_name")
    //@Length(max = 20)
    //@Pattern(regexp = NO_PIPES_REGEX)
    //@NotBlank
    private String firstName;

    //@Audited
    //@Column(name = "last_name")
    //@Length(max = 20)
    //@Pattern(regexp = NO_PIPES_REGEX)
    //@NotBlank
    private String lastName;

    //@Audited
    //@Column(name = "dob")
    private LocalDate dateOfBirth;

    //@Audited
    //@Column(name = "address")
    //@Length(max = 35)
    //@Pattern(regexp = NO_PIPES_REGEX)
    //@NotBlank
    private String addressLine1;

    //@Audited
    //@Column(name = "address2")
    //@Length(max = 35)
    //@Pattern(regexp = NO_PIPES_REGEX)
    private String addressLine2;

    //@Audited
    //@Column(name = "address3")
    //@Length(max = 35)
    //@Pattern(regexp = NO_PIPES_REGEX)
    private String addressLine3;

    //@Audited
    //@Column(name = "address4")
    //@Length(max = 35)
    //@Pattern(regexp = NO_PIPES_REGEX)
    private String addressLine4;

    //@Audited
    //@Column(name = "address5")
    //@Length(max = 35)
    //@Pattern(regexp = NO_PIPES_REGEX)
    private String addressLine5;

    //@Audited
    //@Column(name = "address6")
    //@Null
    private String addressLine6 = null;

    //@Audited
    //@Column(name = "zip")
    //@Length(max = 10)
    //@Pattern(regexp = POSTCODE_REGEX)
    private String postcode;

    //@Audited
    //@Column(name = "h_phone")
    //@Length(max = 15)
    private String phoneNumber;

    //@Audited
    //@Column(name = "w_phone")
    //@Length(max = 15)
    private String workPhone;

    //@Audited
    //@Column(name = "w_ph_local")
    //@Length(max = 4)
    private String workPhoneExtension;

    //@NotNull
    //@Column(name = "responded")
    private boolean responded;

    //@Column(name = "date_excused")
    private LocalDate excusalDate;

    //@Length(max = 1)
    //@Column(name = "excusal_code")
    private String excusalCode;

    //@Column(name = "acc_exc")
    private String excusalRejected;

    //@Column(name = "date_disq")
    private LocalDate disqualifyDate;

    //@Length(max = 1)
    //@Column(name = "disq_code")
    private String disqualifyCode;

    //@Column(name = "user_edtq")
    //@Length(max = 20)
    private String userEdtq;

    //@Length(max = 2000)
    //@Column(name = "notes")
    private String notes;

    //@Column(name = "no_def_pos")
    private Integer noDefPos;

    //@Column(name = "perm_disqual")
    private Boolean permanentlyDisqualify;

    //@Length(max = 1)
    //@Column(name = "reasonable_adj_code")
    private String reasonableAdjustmentCode;

    //@Length(max = 60)
    //@Column(name = "reasonable_adj_msg")
    private String reasonableAdjustmentMessage;

    //@Length(max = 20)
    //@Column(name = "smart_card")
    private String smartCard;

    //@Column(name = "completion_date")
    private LocalDate completionDate;

    //@Audited
    //@Column(name = "sort_code")
    //@Length(max = 6)
    private String sortCode;

    //@Audited
    //@Column(name = "bank_acct_name")
    //@Length(max = 18)
    private String bankAccountName;

    //@Audited
    //@Column(name = "bank_acct_no")
    //@Length(max = 8)
    private String bankAccountNumber;

    //@Audited
    //@Column(name = "bldg_soc_roll_no")
    //@Length(max = 18)
    private String buildingSocietyRollNumber;

    //@Column(name = "welsh")
    private Boolean welsh;

    //@Column(name = "police_check")
    //@Enumerated(EnumType.STRING)
    private PoliceCheck policeCheck;

    //@Length(max = 20)
    //@Column(name = "summons_file")
    private String summonsFile;

    //@Audited
    //@Column(name = "m_phone")
    //@Length(max = 15)
    private String altPhoneNumber;

    //@Audited
    //@Length(max = 254)
    //@Column(name = "h_email")
    private String email;

    //@Column(name = "contact_preference")
    private Integer contactPreference;

    //@Column(name = "notifications")
    private int notifications;

    //@LastModifiedDate
    //@Column(name = "last_update")
    private LocalDateTime lastUpdate;

    //@CreatedDate
    //@Column(name = "date_created", updatable = false)
    private LocalDateTime dateCreated;

    //@Column(name = "optic_reference")
    //@Length(max = 8)
    private String opticRef;

    //@Audited
    //@Column(name = "pending_title")
    //@Length(max = 10)
    //@Pattern(regexp = NO_PIPES_REGEX)
    private String pendingTitle;

    //@Audited
    //@Column(name = "pending_first_name")
    //@Length(max = 20)
    //@Pattern(regexp = NO_PIPES_REGEX)
    private String pendingFirstName;

    //@Audited
    //@Column(name = "pending_last_name")
    //@Length(max = 20)
    //@Pattern(regexp = NO_PIPES_REGEX)
    private String pendingLastName;

    //@OneToMany(mappedBy = "juror", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
//    private Set<JurorPool> associatedPools; //TODO
}
