package org.krzywanski.table.table;


import java.util.List;
import java.util.function.Supplier;

public class DefaultDataPrivder<T> implements DataProviderInterface<T>, SizeProviderInterface{

    int limit;

    DataProviderInterface<T> dataProviderInterface;
    final Supplier<Integer> sizeSupplier;

    public DefaultDataPrivder(int limit, DataProviderInterface<T> dataProviderInterface , Supplier<Integer> sizeSupplier ){
        this.limit = limit;
        this.dataProviderInterface = dataProviderInterface;
        this.sizeSupplier = sizeSupplier;
    }

    @Override
    public int getSize() {
        return sizeSupplier.get();
    }

    @Override
    public List<T> getData(int limit, int offest, SortColumn sortOrder) {
        return dataProviderInterface.getData(limit,offest,sortOrder);
    }
}
