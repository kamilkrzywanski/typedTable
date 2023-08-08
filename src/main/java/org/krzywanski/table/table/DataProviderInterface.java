package org.krzywanski.table.table;

import java.util.List;

public interface DataProviderInterface<T> {


    List<T> getData(long limit, long offest);

    long getSize();

    long getLimit();
}
