package com.placement.ui;

import com.placement.exception.InvalidInputException;
import com.placement.exception.RecordNotFoundException;
import com.placement.model.Application;
import com.placement.model.Drive;
import com.placement.model.Student;
import com.placement.service.ApplicationService;
import com.placement.service.DriveService;
import com.placement.service.StudentService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class StudentMenu {
    private final Student loggedInStudent;
    public Scanner scanner;
    private final StudentService studentService;
    private final DriveService driveService;
    private final ApplicationService applicationService;

    public StudentMenu(Student student) {
        this(student, new Scanner(System.in));
    }

    public StudentMenu(Student student, Scanner scanner) {
        this(student, scanner, new DriveService(), null);
    }

    // Overloaded constructor so PortalApp can pass shared DriveService & ApplicationService instances
    public StudentMenu(Student student, Scanner scanner, DriveService driveService, ApplicationService applicationService) {
        this.loggedInStudent = student;
        this.scanner = scanner;
        this.studentService = new StudentService();
        this.driveService = (driveService != null) ? driveService : new DriveService();
        this.applicationService = (applicationService != null)
                ? applicationService
                : new ApplicationService(this.driveService, id -> {
                    try {
                        return studentService.getStudentById(id);
                    } catch (RecordNotFoundException e) {
                        return null;
                    }
                });
    }

    public void show() {
        int choice = -1;
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
            System.out.print("Enter choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume trailing newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 0 and 7.");
                scanner.nextLine(); // clear bad token from buffer
                continue;
            }

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
        try {
            System.out.print("Enter new CGPA (0 - 10): ");
            double cgpa = scanner.nextDouble();
            System.out.print("Enter active backlogs: ");
            int backlogs = scanner.nextInt();
            scanner.nextLine();

            studentService.updateAcademicProfile(loggedInStudent.getId(), cgpa, backlogs);
            System.out.println("Profile updated successfully!");
        } catch (InputMismatchException e) {
            System.out.println("Error: Please enter valid numeric values for CGPA and backlogs.");
            scanner.nextLine();
        } catch (InvalidInputException | RecordNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleAddPhone() {
        System.out.print("Enter 10-digit phone number: ");
        String phone = scanner.nextLine().trim();
        try {
            studentService.addStudentPhone(loggedInStudent.getId(), phone);
            System.out.println("Phone number added!");
        } catch (InvalidInputException | RecordNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleViewEligibleDrives() {
        List<Drive> eligible = driveService.getEligibleDrives(loggedInStudent);
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
        try {
            System.out.print("Enter Drive ID to apply: ");
            int driveId = scanner.nextInt();
            scanner.nextLine(); // consume trailing newline

            Application app = applicationService.apply(loggedInStudent.getId(), driveId);
            System.out.println("Application submitted! Application ID: " + app.getApplicationId());
        } catch (InputMismatchException e) {
            System.out.println("Error: Drive ID must be a valid integer.");
            scanner.nextLine(); // clear invalid input
        } catch (Exception e) {
            System.out.println("Application failed: " + e.getMessage());
        }
    }

    private void handleTrackApplications() {
        List<Application> myApps = applicationService.getApplicationsForStudent(loggedInStudent.getId());
        System.out.println("\n--- My Applications ---");
        if (myApps.isEmpty()) {
            System.out.println("You have not applied to any drives yet.");
            return;
        }
        for (Application app : myApps) {
            System.out.println(app);
        }
    }
}