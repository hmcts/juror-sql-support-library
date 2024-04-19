package uk.gov.hmcts.juror.support.sql.v2.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "judge", schema = "juror_mod")
public class JudgeEntity {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "owner")
//    @Length(min = 3, max = 3)
    private String owner;

    @Column(name = "code")
//    @Length(max = 4)
    private String code;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "description")
    private String name;

    @Column(name = "last_used")
    private LocalDateTime lastUsed;

}
