package com.backend.pdf_wizard.exception;

import java.io.IOException;

public class StorageException extends RuntimeException {
    public StorageException(String message) {
        super(message);
    }
}
