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
public class CourtroomsDto {

    @JsonProperty("")
    private java.lang.Long id;
    @JsonProperty("owner")
    private java.lang.String owner;
    @JsonProperty("loc_code")
    private java.lang.String locCode;
    @JsonProperty("room_number")
    private java.lang.String roomNumber;
    @JsonProperty("description")
    private java.lang.String description;
}
