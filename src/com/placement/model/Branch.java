package com.placement.model;

import java.io.Serializable;

public class Branch implements Serializable {
    private static final long serialVersionUID = 1L;

    private int branchId;
    private String branchName;
    private String department;

    public Branch(int branchId, String branchName, String department) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.department = department;
    }

    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public String toString() {
        return branchName + " (" + department + ")";
    }
}