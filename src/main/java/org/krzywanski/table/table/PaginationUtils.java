package org.krzywanski.table.table;

public class PaginationUtils {

    DataProvider<?> provider;
    TypedTable<?> tTypedTable;
    int offset = 0;

    public <T> PaginationUtils(DataProvider<T> provider, TypedTable<T> tTypedTable) {
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


        int limit = findCurrentLimit();
        int dataSize = provider != null ? provider.getSize() : 0;

        int lastPage = (int) Math.ceil((double) dataSize / limit);

        int currentPage = offset / limit;

        if (currentPage < lastPage - 1) {
            offset = Math.min(offset + limit, dataSize);
            currentPage++;
        }

        tTypedTable.addData(limit, offset, tTypedTable.getSortColumn());

        return new Pair<>(currentPage + 1, lastPage);
    }

    public Pair<Integer, Integer> prevPageAction() {
        int limit = findCurrentLimit();

        int currentPage = offset / limit;

        if (currentPage > 0) {
            offset = Math.max(0, offset - limit);
            currentPage--;
        }

        tTypedTable.addData(limit, offset, tTypedTable.getSortColumn());

        return new Pair<>(currentPage + 1, (int) Math.ceil((double) (provider != null ? provider.getSize() : 0) / limit));
    }

    public Pair<Integer, Integer> lastPageAction() {
        int limit = findCurrentLimit();
        int dataSize = provider != null ? provider.getSize() : 0;

        int lastPage = (int) Math.ceil((double) dataSize / limit);

        offset = (lastPage - 1) * limit;

        tTypedTable.addData(limit, offset, tTypedTable.getSortColumn());

        return new Pair<>(lastPage, lastPage);
    }

    public Pair<Integer, Integer> firstPageAction() {
        int limit = findCurrentLimit();

        offset = 0;

        tTypedTable.addData(limit, offset, tTypedTable.getSortColumn());

        return new Pair<>(1, (int) Math.ceil((double) (provider != null ? provider.getSize() : 0) / limit));
    }
}
