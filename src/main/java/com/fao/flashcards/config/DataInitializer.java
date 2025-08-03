package com.fao.flashcards.config;

import com.fao.flashcards.domain.model.Card;
import com.fao.flashcards.domain.model.Deck;
import com.fao.flashcards.domain.model.DeckCard;
import com.fao.flashcards.domain.repository.CardRepository;
import com.fao.flashcards.domain.repository.DeckCardRepository;
import com.fao.flashcards.domain.repository.DeckRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
public class DataInitializer {

    @Bean
    @Profile("dev") // Nicht in Testumgebung ausführen
    CommandLineRunner init(CardRepository cardRepository, DeckRepository deckRepository, DeckCardRepository deckCardRepository) {
        return args -> {
            // Karten erstellen
            Card card1 = new Card("Was ist Hava?", "Java ist eine objektorientierte Programmiersprache");
            card1.setTags(new HashSet<>(Arrays.asList("Programmierung", "Java")));
            
            Card card2 = new Card("Was ist Spring Boot?", "Spring Boot ist ein Framework zur Erstellung von Java-Anwendungen");
            card2.setTags(new HashSet<>(Arrays.asList("Programmierung", "Spring", "Java")));
            
            Card card3 = new Card("Was ist React?", "React ist eine JavaScript-Bibliothek zum Erstellen von Benutzeroberflächen");
            card3.setTags(new HashSet<>(Arrays.asList("Programmierung", "Frontend", "JavaScript")));
            
            Card card4 = new Card("Was ist TypeScript?", "TypeScript ist eine typisierte Obermenge von JavaScript");
            card4.setTags(new HashSet<>(Arrays.asList("Programmierung", "Frontend", "JavaScript")));
            
            // Karten speichern
            cardRepository.saveAll(Arrays.asList(card1, card2, card3, card4));
            
            // Decks erstellen
            Deck deck1 = new Deck("Programmiersprachen", "Eine Sammlung von Karteikarten über verschiedene Programmiersprachen");
            deck1.setTags(new HashSet<>(Arrays.asList("Programmierung", "Sprachen")));
            
            Deck deck2 = new Deck("Frontend-Technologien", "Eine Sammlung von Karteikarten über Frontend-Technologien");
            deck2.setTags(new HashSet<>(Arrays.asList("Programmierung", "Frontend")));
            
            // Decks speichern
            deckRepository.saveAll(Arrays.asList(deck1, deck2));
            
            // Karten zu Decks hinzufügen
            DeckCard dc1 = new DeckCard(deck1.getId(), card1.getId());
            DeckCard dc2 = new DeckCard(deck1.getId(), card2.getId());
            DeckCard dc3 = new DeckCard(deck2.getId(), card3.getId());
            DeckCard dc4 = new DeckCard(deck2.getId(), card4.getId());
            
            // DeckCards speichern
            deckCardRepository.saveAll(Arrays.asList(dc1, dc2, dc3, dc4));
        };
    }
}
