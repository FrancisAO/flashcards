package com.fao.flashcards.shared.model.pagination;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Data;

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

    /**
     * Transformiert den Inhalt der Seite mit der gegebenen Mapping-Funktion.
     * 
     * @param mapper die Transformation, die auf jeden Inhaltselement angewendet
     *               wird
     * @param <U>    der Typ des transformierten Inhalts
     * @return eine neue Page mit transformiertem Inhalt
     */
    public <U> Page<U> map(Function<T, U> mapper) {
        List<U> transformedContent = content.stream()
                .map(mapper)
                .collect(Collectors.toList());

        return new Page<>(transformedContent, pageRequest, totalElements);
    }

    public long getNumberOfElements() {
        return totalElements;
    }
}