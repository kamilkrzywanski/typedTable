package org.krzywanski.table.table;

import java.util.List;

public abstract class DataProvider<T> implements DataProviderInterface{

    long limit;

    public DataProvider(long limit){
        this.limit = limit;
    }

    public abstract List<T> getData(long limit, long offest);

    public abstract long getSize();

    public long getLimit(){
        return limit;
    }
}
