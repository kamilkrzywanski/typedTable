package org.krzywanski.table;

import org.krzywanski.table.annot.ReflectionSort;
import org.krzywanski.table.constraints.ActionType;
import org.krzywanski.table.providers.TableDataProvider;
import org.krzywanski.table.utils.Page;

import java.util.ArrayList;
import java.util.Objects;

public class PaginationUtils {

    final TableDataProvider<?> provider;
    final TypedTable<?> tTypedTable;
    int offset = 0;
    int limit = 0;
    public <T> PaginationUtils(TableDataProvider<T> provider, TypedTable<T> tTypedTable) {
        this.provider = provider;
        this.tTypedTable = tTypedTable;
    }

    private Integer findCurrentLimit() {

        if (provider != null) {
            return provider.getLimit();
        }
        return tTypedTable.dataList.size();
    }

    public Page nextPageAction() {
        limit = findCurrentLimit();
        int dataSize = getValue(provider);

        int lastPage = (int) Math.ceil((double) dataSize / limit);

        int currentPage = offset / limit;

        if (currentPage < lastPage - 1) {
            offset = Math.min(offset + limit, dataSize);
            currentPage++;
        }

        localAddData(ActionType.NEXT_PAGE);

        return new Page(currentPage + 1, lastPage, dataSize);
    }

    public Page prevPageAction() {
        limit = findCurrentLimit();

        int currentPage = offset / limit;

        if (currentPage > 0) {
            offset = Math.max(0, offset - limit);
            currentPage--;
        }

        localAddData(ActionType.PREV_PAGE);
        int dataSize = getValue(provider);
        return new Page(currentPage + 1, (int) Math.ceil((double) dataSize / limit), dataSize);
    }

    public Page lastPageAction() {
        limit = findCurrentLimit();
        int dataSize = getValue(provider);

        int lastPage = (int) Math.ceil((double) dataSize / limit);

        offset = (lastPage - 1) * limit;
        localAddData(ActionType.LAST_PAGE);
        return new Page(lastPage, lastPage, dataSize);
    }

    public Page firstPageAction() {
        limit = findCurrentLimit();

        offset = 0;

        localAddData(ActionType.FIRST_PAGE);

        int dataSize = getValue(provider);

        int localLimit = (int) Math.ceil((double) dataSize / limit);
        localLimit = Math.max(1, localLimit);
        return new Page(1, localLimit, dataSize);
    }

    public Page refreshData(int goToIndex) {
        localAddData(ActionType.REFRESH, goToIndex);

        int dataSize = getValue(provider);
        int currentPage = offset / limit;
        currentPage++;
        return new Page(currentPage, limit, dataSize);
    }

    private void localAddData(ActionType actionType) {
        localAddData(actionType, 0);
    }

    private void localAddData(ActionType actionType, int goToIndex) {
        limit = computeLimit(limit, provider);
        tTypedTable.addData(limit, offset, actionType);
        //select table at goToIndex index
        if(goToIndex > 0) {
            tTypedTable.setRowSelectionInterval(goToIndex -1, goToIndex);
        }
    }

    private int getValue(TableDataProvider<?> privder){
        if(privder == null)
            return Objects.requireNonNullElse(tTypedTable.dataList, new ArrayList<>()).size();
        return provider.getSize(tTypedTable.getSearchPhase(), tTypedTable.extraParams);
    }

    /**
     * Compute limit if you use reflection sort
     * @param limit - limit
     * @param provider - provider
     * @return - computed limit
     */
    private int computeLimit(int limit, TableDataProvider<?> provider){
        if(tTypedTable.typeClass.isAnnotationPresent(ReflectionSort.class)){
            if(tTypedTable.getSortColumns().isEmpty())
                if(provider != null)
                    return provider.getLimit();
                else
                    return tTypedTable.currentData.size();
            else
            if(provider != null)
                return provider.reflectionSortLimit();
            else
                return tTypedTable.currentData.size();
        }
        return limit;
    }
}
