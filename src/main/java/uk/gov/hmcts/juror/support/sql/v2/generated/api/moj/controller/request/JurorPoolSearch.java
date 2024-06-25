package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request;

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
public class JurorPoolSearch {

    @JsonProperty("juror_name")
    private java.lang.String jurorName;
    @JsonProperty("juror_number")
    private java.lang.String jurorNumber;
    @JsonProperty("postcode")
    private java.lang.String postcode;
    @JsonProperty("pool_number")
    private java.lang.String poolNumber;
    @JsonProperty("page_number")
    private long pageNumber;
    @JsonProperty("page_limit")
    private long pageLimit;
    @JsonProperty("sort_method")
    private uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.SortMethod sortMethod;
    @JsonProperty("sort_field")
    private SortField sortField;
public enum SortField {
    JUROR_NUMBER,
    FIRST_NAME,
    LAST_NAME,
    POSTCODE,
    COMPLETION_DATE,
}
}
