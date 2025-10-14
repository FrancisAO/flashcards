package com.fao.flashcards.shared.model.pagination;

import lombok.Data;
import java.util.List;

@Data
public class Sort {
    private final List<Order> orders;
    
    public Sort(List<Order> orders) {
        this.orders = orders != null ? orders : List.of();
    }
    
    public static Sort by(String... properties) {
        return new Sort(List.of(properties).stream()
            .map(prop -> new Order(Direction.ASC, prop))
            .toList());
    }
    
    public static Sort unsorted() {
        return new Sort(List.of());
    }
    
    public boolean isSorted() {
        return !orders.isEmpty();
    }
    
    public enum Direction {
        ASC, DESC
    }
    
    @Data
    public static class Order {
        private final Direction direction;
        private final String property;
    }
}