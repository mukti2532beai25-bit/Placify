package com.placement.model;

import com.placement.enums.ApplicationStage;
import com.placement.enums.ResultStatus;
import java.io.Serializable;
import java.time.LocalDate;

public class Application implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int applicationId;
    private final int studentId;
    private final int driveId;
    private final LocalDate appliedDate;
    private ApplicationStage stage;
    private ResultStatus result;
    private boolean offerAccepted;
    private String offerType;

    public Application(int applicationId, int studentId, int driveId, LocalDate appliedDate) {
        this.applicationId = applicationId;
        this.studentId = studentId;
        this.driveId = driveId;
        this.appliedDate = appliedDate;
        this.stage = ApplicationStage.APPLIED;
        this.result = ResultStatus.PENDING;
        this.offerAccepted = false;
        this.offerType = null;
    }

    public int getApplicationId() { return applicationId; }
    public int getStudentId() { return studentId; }
    public int getDriveId() { return driveId; }
    public LocalDate getAppliedDate() { return appliedDate; }
    public ApplicationStage getStage() { return stage; }
    public void setStage(ApplicationStage stage) { this.stage = stage; }
    public ResultStatus getResult() { return result; }
    public void setResult(ResultStatus result) { this.result = result; }
    public boolean isOfferAccepted() { return offerAccepted; }
    public void setOfferAccepted(boolean offerAccepted) { this.offerAccepted = offerAccepted; }
    public String getOfferType() { return offerType; }
    public void setOfferType(String offerType) { this.offerType = offerType; }

    @Override
    public String toString() {
        return String.format("App #%d | student %d -> drive %d | applied %s | stage: %s | result: %s%s",
                applicationId, studentId, driveId, appliedDate, stage, result,
                offerType == null ? "" : " [" + offerType + "]");
    }
    
}