package org.krzywanski.table.table;


import java.util.List;
import java.util.Map;

/**
 * Default implementation of TableDataProvider in case when you don't want to implement your own, and you have separate requests for size and data
 * @param <T> - type of data
 */
public class DefaultDataPrivder<T> implements TableDataProvider<T> {

    int limit;
    int limitCache;
    DataProviderInterface<T> dataProviderInterface;
    final SizeSupplier sizeSupplier;

    public DefaultDataPrivder(int limit, DataProviderInterface<T> dataProviderInterface, SizeSupplier sizeSupplier) {
        this.limit = limit;
        this.dataProviderInterface = dataProviderInterface;
        this.sizeSupplier = sizeSupplier;
    }

    @Override
    public int getSize(String searchString) {
        return sizeSupplier.size(searchString);
    }

    @Override
    public List<T> getData(int limit, int offest, List<SortColumn> sortOrder, String searchString, ActionType actionType, Map<String, String> extraParams) {
        return dataProviderInterface.getData(limit, offest, sortOrder, searchString, actionType, extraParams);
    }

    @Override
    public int getLimit() {
        return limit;
    }
}
