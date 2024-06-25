package uk.gov.hmcts.juror.support.sql.v2.generation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.JudgeEntity;

@AllArgsConstructor
@Getter
public class Judge {
    long id;

    public Judge(JudgeEntity judgeEntity) {
        this(judgeEntity.getId());
    }
}
