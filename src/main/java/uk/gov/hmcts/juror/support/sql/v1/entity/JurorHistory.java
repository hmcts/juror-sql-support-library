package uk.gov.hmcts.juror.support.sql.v1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.gov.hmcts.juror.support.sql.v1.entity.enums.HistoryCodeConverter;
import uk.gov.hmcts.juror.support.sql.v1.entity.enums.HistoryCodeMod;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "juror_history", schema = "juror_mod")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JurorHistory implements Serializable {

    // Constants for History other info text
    public static final String RESPONDED = "Responded";
    public static final String ADDED = "Added to New Pool";

    @Id
    @NotNull
    @Column(name = "id")
    @SequenceGenerator(name = "juror_history_gen", schema = "juror_mod", sequenceName = "juror_history_id_seq",
        allocationSize = 1)
    @GeneratedValue(generator = "juror_history_gen", strategy = GenerationType.SEQUENCE)
    public long id;

    @NotNull
    @Column(name = "juror_number")
    private String jurorNumber;

    @NotNull
    @Column(name = "history_code")
    @Convert(converter = HistoryCodeConverter.class)
    private HistoryCodeMod historyCode;

    @NotNull
    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @NotNull
    @Column(name = "user_id")
    private String createdBy;

    /**
     * Additional text information to provide more detail or context surrounding this history event.
     */
    @Column(name = "other_information")
    private String otherInformation;


    /**
     * Additional date information supporting this history event, for example populate the deferred_to date here when
     * creating a deferral letter printed history event.
     */
    @Column(name = "other_info_date")
    private LocalDate otherInformationDate;


    /**
     * Additional reference data information supporting this history event, for example populate the deferral reason
     * code here when creating a deferral letter printed history event.
     */
    @Column(name = "other_info_reference")
    private String otherInformationRef;

    @Column(name = "pool_number")
    private String poolNumber;

    @PrePersist
    public void prePersist() {
        this.dateCreated = LocalDateTime.now();
    }

    public JurorHistory(String jurorNumber, HistoryCodeMod historyCode, LocalDateTime dateCreated, String createdBy,
                        String otherInformation, String poolNumber) {
        this.jurorNumber = jurorNumber;
        this.historyCode = historyCode;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.otherInformation = otherInformation;
        this.poolNumber = poolNumber;
    }

}