package org.krzywanski.table.providers;


import org.krzywanski.table.SortColumn;
import org.krzywanski.table.constraints.ActionType;
import org.krzywanski.table.providers.DataProviderInterface;
import org.krzywanski.table.providers.SizeSupplier;
import org.krzywanski.table.providers.TableDataProvider;

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
    public int getSize(String searchString, Map<String, String> extraParams) {
        return sizeSupplier.size(searchString, extraParams);
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
