package com.fao.flashcards.domain.repository;

import com.fao.flashcards.domain.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckRepository extends JpaRepository<Deck, String> {
    List<Deck> findByTagsContaining(String tag);
}
