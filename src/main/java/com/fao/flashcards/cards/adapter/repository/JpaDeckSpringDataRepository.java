package com.fao.flashcards.cards.adapter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fao.flashcards.cards.model.Deck;

@Repository
public interface JpaDeckSpringDataRepository extends JpaRepository<Deck, String> {
    List<Deck> findByTagsContaining(String tag);
}
