package org.krzywanski.table.table;

import java.util.List;

public abstract class DataProvider<T> implements DataProviderInterface{

    int limit;

    public DataProvider(int limit){
        this.limit = limit;
    }

    public abstract List<T> getData(int limit, int offest);

    public abstract long getSize();

    public long getLimit(){
        return limit;
    }
}
