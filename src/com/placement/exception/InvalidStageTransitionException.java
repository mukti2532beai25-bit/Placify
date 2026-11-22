package com.placement.exception;

/** Thrown when an admin skips a stage or tries to move an application backwards. */
public class InvalidStageTransitionException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidStageTransitionException(String message) {
         super(message);
         }

    public InvalidStageTransitionException(String message, Throwable cause) {
         super(message, cause);
         }
}
