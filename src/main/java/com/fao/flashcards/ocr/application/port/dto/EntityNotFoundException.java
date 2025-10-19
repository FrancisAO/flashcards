package com.fao.flashcards.ocr.application.port.dto;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String msg) {
        super(msg);
    }

}
