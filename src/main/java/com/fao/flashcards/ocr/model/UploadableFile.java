package com.fao.flashcards.ocr.model;

import java.io.IOException;
import java.io.InputStream;

public interface UploadableFile {

    String getOriginalFilename();
    String getContentType();
    long getSize();
    boolean isEmpty();
    InputStream getInputStream() throws IOException;

    
}
