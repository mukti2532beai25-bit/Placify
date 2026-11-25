
package com.placement.ui;

import com.placement.analytics.FunnelMatrix;
import com.placement.enums.ApplicationStage;
import com.placement.enums.DriveStatus;
import com.placement.enums.ResultStatus;
import com.placement.model.Application;
import com.placement.model.Branch;
import com.placement.model.Company;
import com.placement.model.Drive;
import com.placement.model.EligibilityCriteria;
import com.placement.model.PlacementAdmin;
import com.placement.repository.DataStore;
import com.placement.util.FileHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class AdminMenu {

    private final PlacementAdmin loggedInAdmin;
    private final Scanner scanner;

    public AdminMenu(PlacementAdmin admin, Scanner scanner) {
        this.loggedInAdmin = admin;
        this.scanner = scanner;
    }

    public void show() {

        int choice;

        do {
            System.out.println("\n========== PLACIFY ADMIN MENU ==========");
            System.out.println("1. Create Company");
            System.out.println("2. Create Placement Drive");
            System.out.println("3. Advance Application Stage");
            System.out.println("4. Declare Application Result");
            System.out.println("5. View Placement Funnel");
            System.out.println("6. Export Experiences to CSV");
            System.out.println("7. Backup Experiences");
            System.out.println("0. Logout");

            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    createCompany();
                    break;

                case 2:
                    createDrive();
                    break;

                case 3:
                    advanceApplicationStage();
                    break;

                case 4:
                    declareResult();
                    break;

                case 5:
                    viewPlacementFunnel();
                    break;

                case 6:
                    exportExperiences();
                    break;

                case 7:
                    backupExperiences();
                    break;

                case 0:
                    System.out.println("Admin logged out successfully.");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 0);
    }

    // Create a new company.
    private void createCompany() {

        System.out.println("\n========== CREATE COMPANY ==========");

        String name = readText("Enter company name: ");
        String industry = readText("Enter industry: ");
        String website = readText("Enter website: ");
        String location = readText("Enter headquarters location: ");

        int companyId = DataStore.nextCompanyId();

        Company company = new Company(
                companyId,
                name,
                industry,
                website,
                location
        );

        DataStore.companies.put(companyId, company);

        System.out.println("Company created successfully.");
        System.out.println("Company ID: " + companyId);
    }

    // Create a placement drive and its eligibility criteria.
    private void createDrive() {

        System.out.println("\n========== CREATE PLACEMENT DRIVE ==========");

        if (DataStore.companies.isEmpty()) {
            System.out.println("Create a company before creating a drive.");
            return;
        }

        System.out.println("\nAvailable Companies:");

        for (Company company : DataStore.companies.values()) {
            System.out.println(
                    company.getCompanyId() + ". " + company.getName());
        }

        int companyId = readInt("Enter company ID: ");

        Company company = DataStore.companies.get(companyId);

        if (company == null) {
            System.out.println("Company not found.");
            return;
        }

        String role = readText("Enter job role: ");
        double packageOffered = readDouble(
                "Enter package offered (in LPA): ");

        LocalDate deadline;

        try {
            deadline = LocalDate.parse(
                    readText("Enter application deadline (YYYY-MM-DD): "));
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            return;
        }

        if (deadline.isBefore(LocalDate.now())) {
            System.out.println("Deadline cannot be in the past.");
            return;
        }

        int numberOfRounds = readInt("Enter number of rounds: ");

        if (numberOfRounds <= 0 || packageOffered < 0) {
            System.out.println("Invalid package or number of rounds.");
            return;
        }

        int driveId = DataStore.nextDriveId();

        Drive drive = new Drive(
                driveId,
                company,
                loggedInAdmin.getId(),
                role,
                packageOffered,
                deadline,
                numberOfRounds,
                DriveStatus.UPCOMING
        );

        DataStore.drives.add(drive);

        System.out.println("\nAvailable Branches:");

        for (Branch branch : DataStore.branches.values()) {
            System.out.println(
                    branch.getBranchId() + ". " + branch.getBranchName());
        }

        int branchId = readInt("Enter eligible branch ID: ");

        if (!DataStore.branches.containsKey(branchId)) {
            DataStore.drives.remove(drive);
            System.out.println("Invalid branch. Drive creation cancelled.");
            return;
        }

        double minCGPA = readDouble("Enter minimum CGPA: ");
        int maxBacklogs = readInt("Enter maximum allowed backlogs: ");

        if (minCGPA < 0 || minCGPA > 10 || maxBacklogs < 0) {
            DataStore.drives.remove(drive);
            System.out.println("Invalid eligibility criteria.");
            return;
        }

        EligibilityCriteria criteria = new EligibilityCriteria(
                driveId,
                branchId,
                minCGPA,
                maxBacklogs
        );

        DataStore.eligibilityList.add(criteria);

        System.out.println("\nPlacement drive created successfully.");
        System.out.println("Drive ID: " + driveId);
        System.out.println("Company: " + company.getName());
    }

    // Move an application to its next stage.
    private void advanceApplicationStage() {

        System.out.println("\n========== ADVANCE APPLICATION STAGE ==========");

        int applicationId = readInt("Enter application ID: ");

        Application application = findApplication(applicationId);

        if (application == null) {
            System.out.println("Application not found.");
            return;
        }

        ApplicationStage currentStage = application.getStage();

        if (currentStage == ApplicationStage.RESULT_DECLARED) {
            System.out.println("This application has already reached its final stage.");
            return;
        }

        ApplicationStage[] stages = ApplicationStage.values();

        int nextIndex = currentStage.ordinal() + 1;

        application.setStage(stages[nextIndex]);

        System.out.println("Application stage updated successfully.");
        System.out.println("New stage: " + application.getStage());
    }

    // Declare whether an application resulted in an offer or rejection.
    private void declareResult() {

        System.out.println("\n========== DECLARE RESULT ==========");

        int applicationId = readInt("Enter application ID: ");

        Application application = findApplication(applicationId);

        if (application == null) {
            System.out.println("Application not found.");
            return;
        }

        if (application.getStage() != ApplicationStage.RESULT_DECLARED) {
            System.out.println(
                    "Advance the application to RESULT_DECLARED first.");
            return;
        }

        System.out.println("1. OFFER");
        System.out.println("2. REJECT");

        int choice = readInt("Enter result: ");

        if (choice == 1) {
            application.setResult(ResultStatus.OFFER);
            System.out.println("Offer declared successfully.");

        } else if (choice == 2) {
            application.setResult(ResultStatus.REJECT);
            System.out.println("Rejection declared successfully.");

        } else {
            System.out.println("Invalid result choice.");
        }
    }

    // Find an application using its ID.
    private Application findApplication(int applicationId) {

        for (Application application : DataStore.applications) {
            if (application.getApplicationId() == applicationId) {
                return application;
            }
        }

        return null;
    }

    // Display application counts for every drive and stage.
    private void viewPlacementFunnel() {

        System.out.println("\n========== PLACEMENT FUNNEL ==========");

        if (DataStore.drives.isEmpty()) {
            System.out.println("No placement drives available.");
            return;
        }

        FunnelMatrix funnel = new FunnelMatrix(DataStore.drives.size());

        for (int i = 0; i < DataStore.drives.size(); i++) {

            Drive drive = DataStore.drives.get(i);

            for (Application application : DataStore.applications) {

                if (application.getDriveId() == drive.getDriveId()) {

                    int stageIndex = application.getStage().ordinal();

                    int currentCount = funnel.getCount(i, stageIndex);

                    funnel.setCount(i, stageIndex, currentCount + 1);
                }
            }
        }

        funnel.displayMatrix();
    }

    // Export interview experiences to a CSV file.
    private void exportExperiences() {

        String fileName = readText(
                "Enter CSV file name (example: experiences.csv): ");

        try {
            FileHandler.exportToCSV(
                    DataStore.experiences,
                    fileName
            );

            System.out.println("Experiences exported successfully.");

        } catch (IOException e) {
            System.out.println("Could not export experiences: "
                    + e.getMessage());
        }
    }

    // Create a binary backup of interview experiences.
    private void backupExperiences() {

        String fileName = readText(
                "Enter backup file name (example: experiences.dat): ");

        try {
            FileHandler.backupData(
                    DataStore.experiences,
                    fileName
            );

            System.out.println("Backup created successfully.");

        } catch (IOException e) {
            System.out.println("Could not create backup: "
                    + e.getMessage());
        }
    }

    // Read an integer without leaving a newline in the Scanner.
    private int readInt(String message) {

        while (true) {
            System.out.print(message);

            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    // Read a decimal number.
    private double readDouble(String message) {

        while (true) {
            System.out.print(message);

            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Read text input.
    private String readText(String message) {

        System.out.print(message);
        return scanner.nextLine().trim();
    }
}
