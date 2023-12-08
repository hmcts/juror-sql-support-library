package uk.gov.hmcts.juror.support.sql.entity.jurorresponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "t_reply_type", schema = "juror_mod")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyType implements Serializable {
    @Column(name = "type")
    @Id
    private String type;
    @Column(name = "description")
    private String description;
}
