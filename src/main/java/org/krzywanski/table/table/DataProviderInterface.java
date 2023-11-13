package org.krzywanski.table.table;

import java.util.List;

public interface DataProviderInterface<T> {

    List<T> getData(int limit, int offest, SortColumn sortOrder, String searchString);

}
