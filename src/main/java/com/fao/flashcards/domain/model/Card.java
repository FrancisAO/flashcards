package com.fao.flashcards.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
public class Card {
    
    @Id
    private String id;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String front;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String back;
    
    @ElementCollection
    @CollectionTable(name = "card_tags", joinColumns = @JoinColumn(name = "card_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
    
    public Card(String front, String back) {
        this.front = front;
        this.back = back;
    }
}
