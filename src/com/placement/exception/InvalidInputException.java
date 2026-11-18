package com.placement.exception;

public class InvalidInputException extends Exception {
    private static final long serialVersionUID = 1L;

    private final String errorCode;
    private final String fieldName;
    private final String rejectedValue;

    public InvalidInputException(String message) {
        this("ERR_INVALID_INPUT", "General", "N/A", message);
    }

    public InvalidInputException(String fieldName, String rejectedValue, String message) {
        this("ERR_VALIDATION", fieldName, rejectedValue, message);
    }

    public InvalidInputException(String errorCode, String fieldName, String rejectedValue, String message) {
        super(message);
        this.errorCode = errorCode;
        this.fieldName = fieldName;
        this.rejectedValue = rejectedValue;
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "ERR_PARSE_FAILURE";
        this.fieldName = "Input";
        this.rejectedValue = "Unparseable";
    }

    public String getErrorCode() { return errorCode; }
    public String getFieldName() { return fieldName; }
    public String getRejectedValue() { return rejectedValue; }

    public String getFormattedDetails() {
        return String.format("[%s] Field '%s' (Value: '%s') -> %s",
                errorCode, fieldName, rejectedValue, getMessage());
    }

    @Override
    public String toString() {
        return getFormattedDetails();
    }
}