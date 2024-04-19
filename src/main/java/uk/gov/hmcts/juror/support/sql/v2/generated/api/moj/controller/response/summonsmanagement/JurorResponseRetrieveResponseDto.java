package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.summonsmanagement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JurorResponseRetrieveResponseDto {

    @JsonProperty("record_count")
    private int recordCount;
    @JsonProperty("juror_response")
    private java.util.List<JurorResponseDetails> records;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class JurorResponseDetails {

        @JsonProperty("juror_number")
        private java.lang.String jurorNumber;
        @JsonProperty("pool_number")
        private java.lang.String poolNumber;
        @JsonProperty("first_name")
        private java.lang.String firstName;
        @JsonProperty("last_name")
        private java.lang.String lastName;
        @JsonProperty("postcode")
        private java.lang.String postcode;
        @JsonProperty("court_name")
        private java.lang.String courtName;
        @JsonProperty("officer_assigned")
        private java.lang.String officerAssigned;
        @JsonProperty("reply_status")
        private uk.gov.hmcts.juror.support.sql.v2.generated.api.juror.domain.ProcessingStatus replyStatus;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonProperty("date_received")
        private java.time.LocalDateTime dateReceived;
        @JsonProperty("reply_type")
        private java.lang.String replyType;
    }
}
