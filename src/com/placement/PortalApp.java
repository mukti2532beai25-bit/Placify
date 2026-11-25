
package com.placement;

import com.placement.model.User;
import com.placement.model.Student;
import com.placement.model.PlacementAdmin;
import com.placement.service.AuthService;
import com.placement.ui.StudentMenu;
import com.placement.ui.AdminMenu;
import com.placement.exception.InvalidInputException;
import com.placement.exception.RecordNotFoundException;
import com.placement.repository.DataStore;
import com.placement.model.Branch;

import java.util.Scanner;

public class PortalApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println("       WELCOME TO PLACIFY");
        System.out.println("  Your Placement Management Portal");
        System.out.println("==================================");

        int choice;

        do {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Register as Student");
            System.out.println("2. Login");
            System.out.println("0. Exit");

            choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    registerStudent();
                    break;

                case 2:
                    login();
                    break;

                case 0:
                    System.out.println("Thank you for using Placify!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 0);

        scanner.close();
    }

    // Register a new student.
    private static void registerStudent() {

        System.out.println("\n========== STUDENT REGISTRATION ==========");

        try {
            String firstName = readText("Enter first name: ");
            String lastName = readText("Enter last name: ");
            String email = readText("Enter email: ");
            String password = readText("Enter password: ");

            double cgpa = readDouble("Enter CGPA: ");
            int backlogs = readInt("Enter number of backlogs: ");

            System.out.println("\nAvailable Branches:");

            for (Branch branch : DataStore.branches.values()) {
                System.out.println(
                        branch.getBranchId() + ". "
                                + branch.getBranchName());
            }

            int branchId = readInt("Enter branch ID: ");
            String phone = readText("Enter phone number: ");

            Student student = authService.registerStudent(
                    email,
                    password,
                    firstName,
                    lastName,
                    cgpa,
                    backlogs,
                    branchId,
                    phone
            );

            System.out.println("\nRegistration successful!");
            System.out.println("Welcome, " + student.getFullName());

        } catch (InvalidInputException | RecordNotFoundException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    // Log in an existing student or admin.
    private static void login() {

        System.out.println("\n========== LOGIN ==========");

        String email = readText("Enter email: ");
        String password = readText("Enter password: ");

        try {
            User user = authService.login(email, password);

            System.out.println("\nLogin successful!");

            // Check whether the logged-in user is a student.
            if (user instanceof Student) {

                Student student = (Student) user;

                System.out.println("Welcome, " + student.getFullName());

                StudentMenu studentMenu =
                        new StudentMenu(student, scanner);

                studentMenu.show();

            }

            // Check whether the logged-in user is an admin.
            else if (user instanceof PlacementAdmin) {

                PlacementAdmin admin = (PlacementAdmin) user;

                System.out.println("Welcome, " + admin.getName());

                AdminMenu adminMenu =
                        new AdminMenu(admin, scanner);

                adminMenu.show();

            }

            else {
                System.out.println("Unknown user role.");
            }

        } catch (InvalidInputException | RecordNotFoundException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    // Read an integer.
    private static int readInt(String message) {

        while (true) {
            System.out.print(message);

            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid whole number.");
            }
        }
    }

    // Read a decimal number.
    private static double readDouble(String message) {

        while (true) {
            System.out.print(message);

            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    // Read text.
    private static String readText(String message) {

        System.out.print(message);
        return scanner.nextLine().trim();
    }
}
