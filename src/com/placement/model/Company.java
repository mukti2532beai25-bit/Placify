package com.placement.model;

import java.io.Serializable;

public class Company implements Serializable {
    private static final long serialVersionUID = 1L;

    private int companyId;
    private String name;
    private String industry;
    private String website;
    private String headquartersLocation;
    public Company(int companyId, String name, String industry, String website, String headquartersLocation) {
        this.companyId = companyId;
        this.name = name;
        this.industry = industry;
        this.website = website;
        this.headquartersLocation = headquartersLocation;
    }
    public int getCompanyId() {
        return companyId;
    }
    public String getName() {
        return name;
    }
    public String getIndustry() {
        return industry;
    }
    public String getWebsite() {
        return website;
    }
    public String getHeadquartersLocation() {
        return headquartersLocation;
    }
    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public void setHeadquartersLocation(String headquartersLocation) {
        this.headquartersLocation = headquartersLocation;
    }
 @Override public String toString() {
        return "[" + companyId + "] " + name + " | " + industry + " | " + headquartersLocation;
    }
}
