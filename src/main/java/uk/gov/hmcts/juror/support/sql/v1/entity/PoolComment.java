package uk.gov.hmcts.juror.support.sql.v1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * View of the legacy juror POOL_COMMENTS table data.
 */
@Entity
@Table(name = "pool_comments", schema = "juror_mod")
@NoArgsConstructor
@Getter
@Setter
public class PoolComment implements Serializable {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "pool_comments_gen", schema = "juror_mod", sequenceName = "pool_comments_id_seq",
            allocationSize = 1)
    @GeneratedValue(generator = "pool_comments_gen", strategy = GenerationType.SEQUENCE)
    public long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "pool_no")
    private PoolRequest pool;

    @NotNull
//    @Length(max = 20)
    @Column(name = "user_id")
    private String userId;

    /**
     * Total number of jurors requested for this pool.
     */
    @Column(name = "no_requested")
    private Integer numberRequested;

    @NotNull
//    @Length(max = 80)
    @Column(name = "pcomment")
    private String comment;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @PreUpdate
    private void preUpdate() {
        lastUpdate = LocalDateTime.now();
    }
}

