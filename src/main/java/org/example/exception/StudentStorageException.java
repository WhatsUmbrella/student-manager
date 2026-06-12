package org.example.exception;

public class StudentStorageException extends RuntimeException {
    public StudentStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StudentStorageException(String message) {
        super(message);
    }
}
