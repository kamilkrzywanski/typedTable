package org.krzywanski.table.table;

import java.util.List;

public interface DataProvider<T> {

    long getOffest();

    long getLimit();
    public List<T> getData(long limit, long offest);

    public long getSize();
}
