package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.jurormanagement;

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
public class UpdateAttendanceDto {

    @JsonProperty("commonData")
    private CommonData commonData;
    @JsonProperty("juror")
    private java.util.List<java.lang.String> juror;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class CommonData {

        @JsonProperty("status")
        private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.jurormanagement.UpdateAttendanceStatus
            status;
        @JsonFormat(pattern = "yyyy-MM-dd")
        @JsonProperty("attendanceDate")
        private java.time.LocalDate attendanceDate;
        @JsonProperty("locationCode")
        private java.lang.String locationCode;
        @JsonProperty("checkInTime")
        private java.time.LocalTime checkInTime;
        @JsonProperty("checkOutTime")
        private java.time.LocalTime checkOutTime;
        @JsonProperty("singleJuror")
        private java.lang.Boolean singleJuror;
    }
}
