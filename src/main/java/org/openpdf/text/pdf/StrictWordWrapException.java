package org.openpdf.text.pdf;

/**
 * Exception thrown when strict word wrapping is enabled and text cannot fit
 * within the specified column width without being forced to break mid-word.
 */
public class StrictWordWrapException extends RuntimeException {
    
    /**
     * Constructs a new StrictWordWrapException with the specified detail message.
     * 
     * @param message the detail message
     */
    public StrictWordWrapException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new StrictWordWrapException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public StrictWordWrapException(String message, Throwable cause) {
        super(message, cause);
    }
}