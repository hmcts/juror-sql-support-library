package uk.gov.hmcts.juror.support.sql.v2.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_courts", schema = "juror_mod")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserCourtsEntity.UserCourtsEntityId.class)
public class UserCourtsEntity {

    @Id
    @Column(name = "username", unique = true, length = 20)
    private String username;


    @Id
    @Column(name = "loc_code", unique = true, length = 3)
    private String locCode;


    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    public UserEntity userEntity;


    public static class UserCourtsEntityId {
        private String username;
        private String locCode;
    }

}
