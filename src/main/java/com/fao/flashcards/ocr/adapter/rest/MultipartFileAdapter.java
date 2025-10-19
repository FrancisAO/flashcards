package com.fao.flashcards.ocr.adapter.rest;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.fao.flashcards.ocr.model.UploadableFile;

public class MultipartFileAdapter implements UploadableFile {

    private MultipartFile multipartFile;

    public MultipartFileAdapter(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    @Override
    public String getOriginalFilename() {
        return multipartFile.getOriginalFilename();
    }

    @Override
    public String getContentType() {
        return multipartFile.getContentType();
    }


    @Override
    public long getSize() {
        return multipartFile.getSize();
    }

    @Override
    public boolean isEmpty() {
        return multipartFile.isEmpty();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return multipartFile.getInputStream();
    }
    
}
