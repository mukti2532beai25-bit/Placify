package com.placement.exception;

public class RecordNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    private final String errorCode;
    private final String entityType;
    private final String searchKey;

    public RecordNotFoundException(String message) {
        this("ERR_NOT_FOUND", "Record", "Unknown", message);
    }

    public RecordNotFoundException(String entityType, int missingId) {
        this("ERR_404", entityType, String.valueOf(missingId),
             entityType + " with ID " + missingId + " was not found in the system.");
    }

    public RecordNotFoundException(String entityType, String searchKey) {
        this("ERR_404", entityType, searchKey,
             entityType + " matching '" + searchKey + "' was not found in the system.");
    }

    public RecordNotFoundException(String errorCode, String entityType, String searchKey, String message) {
        super(message);
        this.errorCode = errorCode;
        this.entityType = entityType;
        this.searchKey = searchKey;
    }

    public String getErrorCode() { return errorCode; }
    public String getEntityType() { return entityType; }
    public String getSearchKey() { return searchKey; }

    public String getFormattedDetails() {
        return String.format("[%s] Missing %s (Key: %s) -> %s",
                errorCode, entityType, searchKey, getMessage());
    }

    @Override
    public String toString() {
        return getFormattedDetails();
    }
}