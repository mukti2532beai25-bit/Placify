package com.placement.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;

public class Student extends User {
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private double cgpa;
    private int backlogs;
    private LocalDate dateOfBirth;
    private int graduationBatch;
    private Branch branch;
    private String[] phones;
    public Student(int id, String email, String password, String firstName, String lastName,
                   double cgpa, int backlogs, Branch branch, String primaryPhone) {
        this(id, email, password, firstName, lastName, cgpa, backlogs,
             LocalDate.of(2005, 1, 1), 2027, branch, new String[]{primaryPhone});
    }

    public Student(int id, String email, String password, String firstName, String lastName,
                   double cgpa, int backlogs, LocalDate dateOfBirth, int graduationBatch,
                   Branch branch, String[] phones) {
        super(id, email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.cgpa = cgpa;
        this.backlogs = backlogs;
        this.dateOfBirth = dateOfBirth;
        this.graduationBatch = graduationBatch;
        this.branch = branch;
        this.phones = (phones != null) ? phones : new String[0];
    }

    @Override
    public String getRoleName() {
        return "Student";
    }

    public int getDerivedAge() {
        if (dateOfBirth == null) return 0;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addPhone(String newPhone) {
        String[] updated = Arrays.copyOf(this.phones, this.phones.length + 1);
        updated[updated.length - 1] = newPhone;
        this.phones = updated;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }

    public int getBacklogs() { return backlogs; }
    public void setBacklogs(int backlogs) { this.backlogs = backlogs; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public int getGraduationBatch() { return graduationBatch; }

    public Branch getBranch() { return branch; }
    public void setBranch(Branch branch) { this.branch = branch; }

    public String[] getPhones() { return phones; }
    public void setPhones(String[] phones) { this.phones = phones; }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | Branch: %s | CGPA: %.2f | Backlogs: %d | Phones: %s",
                getId(), getFullName(), branch.getBranchName(), cgpa, backlogs, Arrays.toString(phones));
    }
}