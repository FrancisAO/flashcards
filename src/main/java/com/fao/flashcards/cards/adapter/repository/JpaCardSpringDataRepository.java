package com.fao.flashcards.cards.adapter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fao.flashcards.cards.model.Card;

@Repository
public interface JpaCardSpringDataRepository extends JpaRepository<Card, String> {
    List<Card> findByTagsContaining(String tag);
}
