package com.backend.pdf_wizard.service.storageServices;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {
    void upload(String storedFilename, InputStream inputStream, long size, String contentType) throws IOException;
    InputStream download(String StoredFilename, String originalFilename) throws IOException;
    void delete(String StoredFilename) throws IOException;
}
