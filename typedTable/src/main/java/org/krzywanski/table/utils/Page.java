package org.krzywanski.table.utils;

public class Page {
    public Page(int currentPage, int totalPages, int totalElements) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    int currentPage;
    int totalPages;
    int totalElements;


    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }
}
