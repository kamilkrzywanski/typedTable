package org.krzywanski.table.providers;

public interface TableDataProvider<T> extends DataProviderInterface<T>, SizeProviderInterface {
    int getLimit();

    default int reflectionSortLimit(){
        return -1;
    }

    default boolean isPaginable(){
        return true;
    }
}
