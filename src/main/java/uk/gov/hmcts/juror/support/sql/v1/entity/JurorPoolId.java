package uk.gov.hmcts.juror.support.sql.v1.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class JurorPoolId implements Serializable {

    private String jurorNumber;
    private String poolNumber;

}
