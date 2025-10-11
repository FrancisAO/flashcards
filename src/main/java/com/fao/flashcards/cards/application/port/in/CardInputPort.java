package com.fao.flashcards.cards.application.port.in;

import java.util.List;

import com.fao.flashcards.cards.application.port.dto.CardDTO;

public interface CardInputPort {

    List<CardDTO> getAllCards();

    CardDTO getCardById(String id);

    CardDTO createCard(CardDTO cardDTO);

    CardDTO updateCard(String id, CardDTO cardDTO);

    void deleteCard(String id);

    List<CardDTO> getCardsByTag(String tag);

    List<CardDTO> getCardsByTags(List<String> tags);

    List<String> getAllTags();

}