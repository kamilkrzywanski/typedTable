package org.krzywanski.table.table;


import java.util.List;
import java.util.Optional;

public class DefaultDataPrivder<T> implements DataProviderInterface<T>, SizeProviderInterface{

    int limit;

    DataProviderInterface<T> dataProviderInterface;
    final SizeSupplier sizeSupplier;

    public DefaultDataPrivder(int limit, DataProviderInterface<T> dataProviderInterface , SizeSupplier sizeSupplier ){
        this.limit = limit;
        this.dataProviderInterface = dataProviderInterface;
        this.sizeSupplier = sizeSupplier;
    }

    @Override
    public int getSize(Optional<String> searchString) {
        return sizeSupplier.size(searchString);
    }

    @Override
    public List<T> getData(int limit, int offest, SortColumn sortOrder, Optional<String> searchString) {
        return dataProviderInterface.getData(limit,offest,sortOrder, searchString);
    }
}