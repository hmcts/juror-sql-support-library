package uk.gov.hmcts.juror.support.sql.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.juror.support.generation.generators.code.GenerateGenerationConfig;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

//@Entity
//@Table(name = "pool", schema = "juror_mod")
@NoArgsConstructor
@Getter
@Setter
@GenerateGenerationConfig
public class PoolRequest implements Serializable {

    @Id
//    @NotNull
    @Column(name = "pool_no")
//    @Length(max = 9)
    private String poolNumber;

    /**
     * Composite primary key. -
     * owner in conjunction with poolNumber will identify unique records.
     * generally there will be two records per pool request,
     * one owned by the court and one owned by the central summonsing bureau.
     */
//    @NotNull
    @Column(name = "owner")
//    @Length(max = 3)
    private String owner;

    /**
     * Location code for the specific court the pool is being requested for.
     */
    private String courtLocation;

    /**
     * The date the jurors will be required to first attend court to start their service.
     */
//    @NotNull
    @Column(name = "return_date")
    private LocalDate returnDate;

    /**
     * Total number of jurors requested for this pool.
     */
    @Column(name = "no_requested")
    private Integer numberRequested;

    @ManyToOne
    @JoinColumn(name = "pool_type")
    private String poolType;

    @Column(name = "attend_time")
    private LocalDateTime attendTime;

    @Column(name = "new_request")
    private Character newRequest;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @Column(name = "additional_summons")

    private Integer additionalSummons;

    /**
     * Total number of jurors requested for this pool, before subtracting any deferrals used.
     */
    @Column(name = "total_no_required")
    private int totalNoRequired;

    @Column(name = "nil_pool")
    private boolean nilPool;

    @Column(name = "date_created", updatable = false)
    private LocalDateTime dateCreated;

    @OneToMany(mappedBy = "pool", cascade = CascadeType.REMOVE)
    private Set<PoolComment> poolComments;

}
