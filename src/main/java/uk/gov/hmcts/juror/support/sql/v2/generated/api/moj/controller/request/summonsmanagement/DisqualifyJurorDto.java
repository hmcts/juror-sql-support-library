package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.summonsmanagement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DisqualifyJurorDto {

    @JsonProperty("replyMethod")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.ReplyMethod replyMethod;
    @JsonProperty("code")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.DisqualifyCodeEnum code;
}
