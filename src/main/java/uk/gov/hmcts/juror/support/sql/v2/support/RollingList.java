package uk.gov.hmcts.juror.support.sql.v2.support;

import java.util.ArrayList;
import java.util.List;

public class RollingList<T> {

    private ArrayList<T> data;

    int currentIndex = 0;

    public RollingList(List<T> data) {
        this.data = new ArrayList<>(data);
    }

    public T getNext() {
        if (currentIndex >= data.size()) {
            currentIndex = 0;
        }
        return data.get(currentIndex++);
    }
}
