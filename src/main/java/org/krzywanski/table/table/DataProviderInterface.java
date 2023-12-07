package org.krzywanski.table.table;

import java.util.List;
import java.util.Map;

public interface DataProviderInterface<T> {

    List<T> getData(int limit, int offest, List<SortColumn> sortOrder, String searchString, ActionType actionType, Map<String, String> extraParams);

}
