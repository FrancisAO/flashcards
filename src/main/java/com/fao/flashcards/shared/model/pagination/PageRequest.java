package com.fao.flashcards.shared.model.pagination;

import lombok.Data;

@Data
public class PageRequest {
    private final int page;
    private final int size;
    private final Sort sort;

    public PageRequest(int page, int size) {
        this(page, size, Sort.unsorted());
    }

    public PageRequest(int page, int size, Sort sort) {
        this.page = Math.max(0, page);
        this.size = Math.max(1, size);
        this.sort = sort != null ? sort : Sort.unsorted();
    }

    public long getOffset() {
        return (long) page * size;
    }

    public int getPageSize() {
        return size;
    }
}