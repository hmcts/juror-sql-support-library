package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial;

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
public class EmpanelListDto {

    @JsonProperty("total_jurors_for_empanel")
    private int totalJurorsForEmpanel;
    @JsonProperty("empanel_list")
    private java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial.EmpanelDetailsDto> empanelList;
}
