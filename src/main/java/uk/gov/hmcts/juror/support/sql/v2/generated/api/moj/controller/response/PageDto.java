package uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response;


import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class PageDto<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private int size;
    private int number;
    private Sort sort;
    private int numberOfElements;
    private boolean first;
    private boolean empty;


    public PageDto(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.last = page.isLast();
        this.size = page.getSize();
        this.number = page.getNumber();
        this.sort = new Sort(page);
        this.numberOfElements = page.getNumberOfElements();
        this.first = page.isFirst();
        this.empty = page.isEmpty();
    }

    @Data
    public class Sort {
        private boolean empty;
        private boolean sorted;
        private boolean unsorted;

        public Sort(Page<T> page) {
            this.empty = page.getSort().isEmpty();
            this.sorted = page.getSort().isSorted();
            this.unsorted = page.getSort().isUnsorted();
        }
    }
}