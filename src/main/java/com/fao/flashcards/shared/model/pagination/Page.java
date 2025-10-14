package com.fao.flashcards.shared.model.pagination;

import lombok.Data;
import java.util.List;

@Data
public class Page<T> {
    private final List<T> content;
    private final PageRequest pageRequest;
    private final long totalElements;
    
    public Page(List<T> content, PageRequest pageRequest, long totalElements) {
        this.content = content;
        this.pageRequest = pageRequest;
        this.totalElements = totalElements;
    }
    
    public int getNumber() {
        return pageRequest.getPage();
    }
    
    public int getSize() {
        return pageRequest.getSize();
    }
    
    public int getTotalPages() {
        return (int) Math.ceil((double) totalElements / pageRequest.getSize());
    }
    
    public boolean isFirst() {
        return pageRequest.getPage() == 0;
    }
    
    public boolean isLast() {
        return getNumber() >= getTotalPages() - 1;
    }
    
    public boolean hasNext() {
        return !isLast();
    }
    
    public boolean hasPrevious() {
        return !isFirst();
    }
}