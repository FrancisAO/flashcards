package com.fao.flashcards.cards.application.port.types;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String msg) {
        super(msg);
    }

}
