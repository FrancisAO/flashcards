package com.fao.flashcards.domain.repository;

import com.fao.flashcards.domain.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    List<Card> findByTagsContaining(String tag);
}
