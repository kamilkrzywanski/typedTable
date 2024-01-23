package org.krzywanski.table.providers;

import org.krzywanski.table.*;
import org.krzywanski.table.constraints.ActionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class ListDataProvider<T> implements TableDataProvider<T> {
    List<T> data;
    final Supplier<List<T>> dataSupplier;

    public ListDataProvider(Supplier<List<T>> dataSupplier) {
        this.dataSupplier = dataSupplier;
        this.data = Objects.requireNonNullElse(dataSupplier.get(), new ArrayList<>());

    }


    @Override
    public List<T> getData(int limit, int offest, List<SortColumn> sortOrder, String searchString, ActionType actionType, Map<String, String> extraParams) {
        this.data = Objects.requireNonNullElse(dataSupplier.get(), new ArrayList<>());
        return data;
    }

    @Override
    public int getSize(String searchString, Map<String, String> extraParams) {
        return data.size();
    }

    @Override
    public int getLimit() {
        return -1;
    }

}
