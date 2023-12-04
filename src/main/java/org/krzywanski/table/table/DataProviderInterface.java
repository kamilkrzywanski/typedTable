package org.krzywanski.table.table;

import java.util.List;
import java.util.Optional;

public interface DataProviderInterface<T> {

    List<T> getData(int limit, int offest, SortColumn sortOrder, String searchString, ActionType actionType);

}
