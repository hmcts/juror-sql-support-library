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
public class RetrieveAttendanceDetailsDto {

    @JsonProperty("commonData")
    private CommonData commonData;
    @JsonProperty("juror")
    private java.util.List<java.lang.String> juror;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class CommonData {

        @JsonProperty("tag")
        private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.jurormanagement.RetrieveAttendanceDetailsTag
            tag;
        @JsonFormat(pattern = "yyyy-MM-dd")
        @JsonProperty("attendanceDate")
        private java.time.LocalDate attendanceDate;
        @JsonProperty("locationCode")
        private java.lang.String locationCode;
    }

}