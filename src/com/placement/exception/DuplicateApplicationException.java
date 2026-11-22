package com.placement.exception;

/** Thrown when a student applies more than once to the same Drive. */
public class DuplicateApplicationException extends Exception {
    private static final long serialVersionUID = 1L;

    public DuplicateApplicationException(String message) { 
        super(message); 
    }

    public DuplicateApplicationException(String message, Throwable cause) { 
        super(message, cause); 
    }
}