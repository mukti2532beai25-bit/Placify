package com.placement.exception;

/** Thrown when a student's branch, CGPA or backlogs violate a drive's EligibilityCriteria. */
public class NotEligibleException extends Exception {
    private static final long serialVersionUID = 1L;

    public NotEligibleException(String message) { 
        super(message);
     }

    public NotEligibleException(String message, Throwable cause) { 
        super(message, cause);
     }
}
