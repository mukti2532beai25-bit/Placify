package com.placement.ui;

import com.placement.exception.InvalidInputException;
import com.placement.exception.RecordNotFoundException;
import com.placement.model.Application;
import com.placement.model.Drive;
import com.placement.model.Student;
import com.placement.repository.DataStore;
import com.placement.service.ApplicationService;
import com.placement.service.DriveService;
import com.placement.service.StudentService;
import com.placement.util.InputHelper;

import java.util.List;
import java.util.Scanner;

public class StudentMenu {
    private final Student loggedInStudent;
    private final Scanner scanner;
    private final StudentService studentService;
    private final DriveService driveService;
    private final ApplicationService applicationService;

    public StudentMenu(Student student, Scanner scanner) {
        this.loggedInStudent = student;
        this.scanner = scanner;
        this.studentService = new StudentService();
        this.driveService = new DriveService();
        this.applicationService = new ApplicationService();
    }

    public void show() {
        int choice;
        do {
            System.out.println("\n==========================================");
            System.out.println(" STUDENT DASHBOARD - " + loggedInStudent.getFullName());
            System.out.println("==========================================");
            System.out.println("1. View My Profile");
            System.out.println("2. Update CGPA / Backlogs");
            System.out.println("3. Add Additional Phone Number");
            System.out.println("4. View Eligible Placement Drives");
            System.out.println("5. Apply to a Drive");
            System.out.println("6. Track My Applications");
            System.out.println("7. Open Interview Prep Archive");
            System.out.println("0. Logout");

            choice = InputHelper.readInt(scanner, "Enter choice: ");

            switch (choice) {
                case 1:
                    System.out.println("\n" + loggedInStudent);
                    System.out.println("Age: " + loggedInStudent.getDerivedAge()
                            + " | Batch: " + loggedInStudent.getGraduationBatch());
                    break;

                case 2:
                    handleUpdateAcademic();
                    break;

                case 3:
                    handleAddPhone();
                    break;

                case 4:
                    handleViewEligibleDrives();
                    break;

                case 5:
                    handleApplyToDrive();
                    break;

                case 6:
                    handleTrackApplications();
                    break;

                case 7:
                    new ArchiveMenu(loggedInStudent, scanner).show();
                    break;

                case 0:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid option. Please choose between 0 and 7.");
            }
        } while (choice != 0);
    }

    private void handleUpdateAcademic() {
        double cgpa = InputHelper.readDouble(scanner, "Enter new CGPA (0 - 10): ");
        int backlogs = InputHelper.readInt(scanner, "Enter active backlogs: ");
        try {
            studentService.updateAcademicProfile(loggedInStudent.getId(), cgpa, backlogs);
            System.out.println("Profile updated successfully!");
        } catch (InvalidInputException | RecordNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleAddPhone() {
        String phone = InputHelper.readNonEmptyString(scanner, "Enter 10-digit phone number: ");
        try {
            studentService.addStudentPhone(loggedInStudent.getId(), phone);
            System.out.println("Phone number added!");
        } catch (InvalidInputException | RecordNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleViewEligibleDrives() {
        List<Drive> eligible = driveService.getEligibleDrivesForStudent(loggedInStudent);
        if (eligible.isEmpty()) {
            System.out.println("No eligible drives available right now.");
            return;
        }
        System.out.println("\n--- Eligible Drives ---");
        for (Drive d : eligible) {
            System.out.println(d);
        }
    }

    private void handleApplyToDrive() {
        int driveId = InputHelper.readInt(scanner, "Enter Drive ID to apply: ");
        try {
            Application app = applicationService.apply(loggedInStudent.getId(), driveId);
            System.out.println("Application submitted! Application ID: " + app.getApplicationId());
        } catch (Exception e) {
            System.out.println("Application failed: " + e.getMessage());
        }
    }

    private void handleTrackApplications() {
        boolean found = false;
        System.out.println("\n--- My Applications ---");
        for (Application app : DataStore.applications) {
            if (app.getStudentId() == loggedInStudent.getId()) {
                System.out.println(app);
                found = true;
            }
        }
        if (!found) {
            System.out.println("You have not applied to any drives yet.");
        }
    }
}