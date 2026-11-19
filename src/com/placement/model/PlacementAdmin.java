package com.placement.model;

public class PlacementAdmin extends User {
    private static final long serialVersionUID = 1L;

    private String name;
    private String designation;
    private String phone;
    private String extension;

    public PlacementAdmin(int id, String email, String password, String name,
                          String designation, String phone, String extension) {
        super(id, email, password);
        this.name = name;
        this.designation = designation;
        this.phone = phone;
        this.extension = extension;
    }

    @Override
    public String getRoleName() {
        return "Admin";
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getExtension() { return extension; }
    public void setExtension(String extension) { this.extension = extension; }

    @Override
    public String toString() {
        return String.format("Admin ID: %d | %s (%s) | Email: %s | Phone: %s",
                getId(), name, designation, getEmail(), phone);
    }
}