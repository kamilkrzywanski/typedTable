package org.krzywanski.table;

import org.krzywanski.table.constraints.ActionType;

import java.util.List;
import java.util.Map;

public interface DataProviderInterface<T> {
    /**
     * Provider for table data - should return list of objects
     * @param limit - imit of data
     * @param offest - offset of data
     * @param sortOrder - sort order
     * @param searchString - search string
     * @param actionType - action type
     * @param extraParams - extra params
     * @return
     */
    List<T> getData(int limit, int offest, List<SortColumn> sortOrder, String searchString, ActionType actionType, Map<String, String> extraParams);

}
