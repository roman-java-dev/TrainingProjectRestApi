package com.example.trainingprojectrestapi.exception;

/**
 * Exception thrown to indicate a problem related to file operations.
 */
public class FileOperationException extends RuntimeException {
    /**
     * Constructs a new {@code FileOperationException} with the specified detail message.
     *
     * @param message The error message.
     */
    public FileOperationException(String message) {
        super(message);
    }
}
