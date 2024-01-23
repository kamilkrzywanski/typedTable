package org.krzywanski.table;

public interface TableDataProvider<T> extends DataProviderInterface<T>, SizeProviderInterface {
    int getLimit();

    default int reflectionSortLimit(){
        return -1;
    }
}
