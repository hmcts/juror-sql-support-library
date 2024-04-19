package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.authentication;

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
public class UserCourtDto {

    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.authentication.CourtDto primaryCourt;
    private java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.authentication.CourtDto> satelliteCourts;
}
