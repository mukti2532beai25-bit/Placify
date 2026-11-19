package com.placement.model;

import java.io.Serializable;

public class EligibilityCriteria implements Serializable {
    private static final long serialVersionUID = 1L;

    private int driveId;
    private int branchId;
    private double minCGPA;
    private int maxBacklogs;

    public EligibilityCriteria(int driveId, int branchId, double minCGPA, int maxBacklogs) {
        this.driveId = driveId;
        this.branchId = branchId;
        this.minCGPA = minCGPA;
        this.maxBacklogs = maxBacklogs;
    }

    // Getters
    public int getDriveId() {
        return driveId;
    }

    public int getBranchId() {
        return branchId;
    }

    public double getMinCGPA() {
        return minCGPA;
    }

    public int getMaxBacklogs() {
        return maxBacklogs;
    }

    // Setters
    public void setDriveId(int driveId) {
        this.driveId = driveId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public void setMinCGPA(double minCGPA) {
        this.minCGPA = minCGPA;
    }

    public void setMaxBacklogs(int maxBacklogs) {
        this.maxBacklogs = maxBacklogs;
    }

    // Returns true only if BOTH the CGPA and backlog conditions are met
    public boolean isSatisfiedBy(double cgpa, int backlogs) {
        return cgpa >= minCGPA && backlogs <= maxBacklogs;
    }

    @Override
    public String toString() {
        return String.format("Eligibility[Drive %d, Branch %d, minCGPA %.2f, maxBacklogs %d]",
                driveId, branchId, minCGPA, maxBacklogs);
    }
}