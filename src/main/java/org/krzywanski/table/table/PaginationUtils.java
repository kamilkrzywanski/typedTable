package org.krzywanski.table.table;

import java.util.Optional;

public class PaginationUtils {

    TableDataProvider<?> provider;
    TypedTable<?> tTypedTable;
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
        return tTypedTable.currentData.size();
    }

    public Pair<Integer, Integer> nextPageAction() {
        limit = findCurrentLimit();
        int dataSize = getValue(provider);

        int lastPage = (int) Math.ceil((double) dataSize / limit);

        int currentPage = offset / limit;

        if (currentPage < lastPage - 1) {
            offset = Math.min(offset + limit, dataSize);
            currentPage++;
        }

        localAddData(ActionType.NEXT_PAGE);

        return new Pair<>(currentPage + 1, lastPage);
    }

    public Pair<Integer, Integer> prevPageAction() {
        limit = findCurrentLimit();

        int currentPage = offset / limit;

        if (currentPage > 0) {
            offset = Math.max(0, offset - limit);
            currentPage--;
        }

        localAddData(ActionType.PREV_PAGE);

        return new Pair<>(currentPage + 1, (int) Math.ceil((double) getValue(provider) / limit));
    }

    public Pair<Integer, Integer> lastPageAction() {
        limit = findCurrentLimit();
        int dataSize = getValue(provider);

        int lastPage = (int) Math.ceil((double) dataSize / limit);

        offset = (lastPage - 1) * limit;
        localAddData(ActionType.LAST_PAGE);
        return new Pair<>(lastPage, lastPage);
    }

    public Pair<Integer, Integer> firstPageAction() {
        limit = findCurrentLimit();

        offset = 0;

        localAddData(ActionType.FIRST_PAGE);

        return new Pair<>(1, (int) Math.ceil((double) getValue(provider) / limit));
    }

    private void localAddData(ActionType actionType) {
        tTypedTable.addData(limit, offset, actionType);
    }

    private int getValue(TableDataProvider<?> privder){
        if(privder == null)
            return 0;
        return provider.getSize(tTypedTable.getSearchPhase());
    }
}
