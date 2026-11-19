package com.placement.model;

import com.placement.enums.DriveStatus;
import java.io.Serializable;
import java.time.LocalDate;

public class Drive implements Serializable {
    private static final long serialVersionUID = 1L;

    private int driveId;
    private Company company;
    private int adminId;
    private String role;
    private double packageOffered;
    private LocalDate applicationDeadline;
    private int numberOfRounds;
    private DriveStatus driveStatus;

    // Parameterized Constructor
    public Drive(int driveId, Company company, int adminId,
                 String role, double packageOffered,
                 LocalDate applicationDeadline,
                 int numberOfRounds, DriveStatus driveStatus) {

        this.driveId = driveId;
        this.company = company;
        this.adminId = adminId;
        this.role = role;
        this.packageOffered = packageOffered;
        this.applicationDeadline = applicationDeadline;
        this.numberOfRounds = numberOfRounds;
        this.driveStatus = driveStatus;
    }

    // Getters
    public int getDriveId() {
        return driveId;
    }

    public Company getCompany() {
        return company;
    }

    public int getAdminId() {
        return adminId;
    }

    public String getRole() {
        return role;
    }

    public double getPackageOffered() {
        return packageOffered;
    }

    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public DriveStatus getDriveStatus() {
        return driveStatus;
    }

    // Setters
    public void setDriveId(int driveId) {
        this.driveId = driveId;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPackageOffered(double packageOffered) {
        this.packageOffered = packageOffered;
    }

    public void setApplicationDeadline(LocalDate applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public void setDriveStatus(DriveStatus driveStatus) {
        this.driveStatus = driveStatus;
    }
    @Override
public String toString() {
    return String.format(
        "Drive #%d | %s | %s | %.1f LPA | Deadline %s | %d rounds | %s",
        driveId, company.getName(), role, packageOffered,
        applicationDeadline, numberOfRounds, driveStatus
    );
}
}
