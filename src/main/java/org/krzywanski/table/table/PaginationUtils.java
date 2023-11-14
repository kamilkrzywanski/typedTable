package org.krzywanski.table.table;

import java.util.Optional;

public class PaginationUtils {

    DefaultDataPrivder<?> provider;
    TypedTable<?> tTypedTable;
    int offset = 0;
    int limit = 0;
    public <T> PaginationUtils(DefaultDataPrivder<T> provider, TypedTable<T> tTypedTable) {
        this.provider = provider;
        this.tTypedTable = tTypedTable;
    }

    private Integer findCurrentLimit() {

        if (provider != null) {
            return provider.limit;
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

        localAddData();

        return new Pair<>(currentPage + 1, lastPage);
    }

    public Pair<Integer, Integer> prevPageAction() {
        limit = findCurrentLimit();

        int currentPage = offset / limit;

        if (currentPage > 0) {
            offset = Math.max(0, offset - limit);
            currentPage--;
        }

        localAddData();

        return new Pair<>(currentPage + 1, (int) Math.ceil((double) getValue(provider) / limit));
    }

    public Pair<Integer, Integer> lastPageAction() {
        limit = findCurrentLimit();
        int dataSize = getValue(provider);

        int lastPage = (int) Math.ceil((double) dataSize / limit);

        offset = (lastPage - 1) * limit;
        localAddData();
        return new Pair<>(lastPage, lastPage);
    }

    public Pair<Integer, Integer> firstPageAction() {
        limit = findCurrentLimit();

        offset = 0;

        localAddData();

        return new Pair<>(1, (int) Math.ceil((double) getValue(provider) / limit));
    }

    private void localAddData() {
        tTypedTable.addData(limit, offset);
    }

    private int getValue(DefaultDataPrivder<?> privder){
        if(privder == null)
            return 0;
        return provider.getSize(Optional.ofNullable(tTypedTable.getSearchPhase()));
    }
}
