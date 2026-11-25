
package com.placement.ui;

import com.placement.model.Student;
import java.util.Scanner;
import com.placement.model.InterviewExperience;
import com.placement.repository.DataStore;
import com.placement.model.Application;
import com.placement.service.ExperienceService;
import com.placement.enums.ApplicationStage;
import com.placement.enums.Difficulty;
import com.placement.model.RoundDetail;
import com.placement.model.TopicTag;
import com.placement.enums.RoundType;
import java.util.ArrayList;
import java.util.List;

public class ArchiveMenu {

    private final Student loggedInStudent;
    private final Scanner scanner;

    public ArchiveMenu(Student student, Scanner scanner) {
        this.loggedInStudent = student;
        this.scanner = scanner;
    }

    public void show() {

        int choice;

        do {
            System.out.println("\n================================");
            System.out.println("     INTERVIEW PREP ARCHIVE");
            System.out.println("================================");

            System.out.println("Welcome, " + loggedInStudent.getFullName());

            System.out.println("1. Log Interview Experience");
            System.out.println("2. View All Experiences");
            System.out.println("3. Search Experiences");
            System.out.println("4. View Topic Frequencies");
            System.out.println("0. Back to Student Dashboard");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    logInterviewExperience();
                    break;

                case 2:
                    viewAllExperiences();
                    break;

                case 3:
                    searchExperiences();
                    break;

                case 4:
                    viewTopicFrequencies();
                    break;

                case 0:
                    System.out.println("Returning to Student Dashboard...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 0);
    }

    private void viewAllExperiences() {

        if (DataStore.experiences.isEmpty()) {
            System.out.println("\nNo interview experiences available yet.");
            return;
        }

        System.out.println("\n======= ALL INTERVIEW EXPERIENCES =======");

        for (InterviewExperience experience : DataStore.experiences) {

            System.out.println("\nExperience ID: " + experience.getExperienceId());
            System.out.println("Difficulty: " + experience.getDifficulty());
            System.out.println("Tips: " + experience.getTips());
            System.out.println("Anonymous: " + experience.isAnonymous());
            System.out.println("Date Posted: " + experience.getDatePosted());

            System.out.println("Number of Rounds: " + experience.getRounds().size());
        }
    }

    private void searchExperiences() {

        System.out.println("\n--- SEARCH EXPERIENCES ---");
        System.out.println("1. Search by Tips");
        System.out.println("2. Search by Company");
        System.out.println("3. Search by Difficulty");
        System.out.print("Enter choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        ExperienceService service = new ExperienceService();

        if (choice == 1) {

            System.out.print("Enter keyword: ");
            String keyword = scanner.nextLine();

            System.out.println(service.searchArchive(
                    DataStore.experiences, keyword));

        } else if (choice == 2) {

            System.out.print("Enter company name: ");
            String companyName = scanner.nextLine();

            System.out.println(service.searchByCompany(
                    DataStore.experiences,
                    DataStore.applications,
                    DataStore.drives,
                    companyName));

        } else if (choice == 3) {

            System.out.println("Select Difficulty:");
            System.out.println("1. EASY");
            System.out.println("2. MEDIUM");
            System.out.println("3. HARD");
            System.out.print("Enter choice: ");

            int difficultyChoice = scanner.nextInt();
            scanner.nextLine();

            Difficulty difficulty;

            switch (difficultyChoice) {
                case 1:
                    difficulty = Difficulty.EASY;
                    break;
                case 2:
                    difficulty = Difficulty.MEDIUM;
                    break;
                case 3:
                    difficulty = Difficulty.HARD;
                    break;
                default:
                    System.out.println("Invalid difficulty!");
                    return;
            }

            boolean found = false;

            for (InterviewExperience experience : DataStore.experiences) {

                if (experience.getDifficulty() == difficulty) {

                    System.out.println("Experience ID: "
                            + experience.getExperienceId()
                            + ", Difficulty: "
                            + experience.getDifficulty()
                            + ", Tips: "
                            + experience.getTips());

                    found = true;
                }
            }

            if (!found) {
                System.out.println("No experiences found for this difficulty.");
            }

        } else {
            System.out.println("Invalid choice!");
        }
    }

    private void viewTopicFrequencies() {
        ExperienceService service = new ExperienceService();

        System.out.println("\n--- TOPIC FREQUENCIES ---");

        java.util.TreeMap<String, Integer> frequencies = service.countTopicFrequencies(DataStore.experiences);

        if (frequencies.isEmpty()) {
            System.out.println("No topic data available yet.");
            return;
        }

        for (java.util.Map.Entry<String, Integer> entry : frequencies.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    private Application selectEligibleApplication() {

        System.out.println("\n--- Your Eligible Applications ---");

        boolean found = false;

        for (Application app : DataStore.applications) {

            if (app.getStudentId() == loggedInStudent.getId()
                    && app.getStage() == ApplicationStage.RESULT_DECLARED) {

                System.out.println(app);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No eligible applications found.");
            return null;
        }

        System.out.print("Enter Application ID: ");
        int applicationId = scanner.nextInt();
        scanner.nextLine();

        for (Application app : DataStore.applications) {

            if (app.getApplicationId() == applicationId
                    && app.getStudentId() == loggedInStudent.getId()
                    && app.getStage() == ApplicationStage.RESULT_DECLARED) {

                return app;
            }
        }

        System.out.println("Invalid application selection!");
        return null;
    }

    private void logInterviewExperience() {

        Application application = selectEligibleApplication();

        if (application == null) {
            return;
        }

        System.out.println("\n--- LOG INTERVIEW EXPERIENCE ---");

        System.out.println("Select Overall Difficulty:");
        System.out.println("1. EASY");
        System.out.println("2. MEDIUM");
        System.out.println("3. HARD");

        System.out.print("Enter choice: ");
        int difficultyChoice = scanner.nextInt();
        scanner.nextLine();

        Difficulty difficulty;

        switch (difficultyChoice) {
            case 1:
                difficulty = Difficulty.EASY;
                break;
            case 2:
                difficulty = Difficulty.MEDIUM;
                break;
            case 3:
                difficulty = Difficulty.HARD;
                break;
            default:
                System.out.println("Invalid difficulty!");
                return;
        }

        System.out.print("Enter interview tips: ");
        String tips = scanner.nextLine();

        System.out.print("Post anonymously? (yes/no): ");
        boolean anonymous = scanner.nextLine().equalsIgnoreCase("yes");

        List<RoundDetail> rounds = new ArrayList<>();

        System.out.print("How many interview rounds did you have? ");
        int numberOfRounds = scanner.nextInt();
        scanner.nextLine();

        if (numberOfRounds <= 0) {
            System.out.println("You must enter at least one round.");
            return;
        }

        for (int i = 1; i <= numberOfRounds; i++) {

            System.out.println("\n--- ROUND " + i + " ---");

            System.out.println("Select Round Type:");
            System.out.println("1. TECHNICAL");
            System.out.println("2. HR");
            System.out.println("3. CODING");
            System.out.println("4. GD");
            System.out.println("5. APTITUDE");

            System.out.print("Enter choice: ");
            int roundChoice = scanner.nextInt();
            scanner.nextLine();

            RoundType roundType;

            switch (roundChoice) {
                case 1:
                    roundType = RoundType.TECHNICAL;
                    break;
                case 2:
                    roundType = RoundType.HR;
                    break;
                case 3:
                    roundType = RoundType.CODING;
                    break;
                case 4:
                    roundType = RoundType.GD;
                    break;
                case 5:
                    roundType = RoundType.APTITUDE;
                    break;
                default:
                    System.out.println("Invalid round type!");
                    return;
            }

            System.out.print("Enter mode (Online/Offline): ");
            String mode = scanner.nextLine();

            System.out.print("Enter duration in minutes: ");
            int duration = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter questions asked: ");
            String questions = scanner.nextLine();

            System.out.println("Select Round Difficulty:");
            System.out.println("1. EASY");
            System.out.println("2. MEDIUM");
            System.out.println("3. HARD");

            System.out.print("Enter choice: ");
            int roundDifficultyChoice = scanner.nextInt();
            scanner.nextLine();

            Difficulty roundDifficulty;

            switch (roundDifficultyChoice) {
                case 1:
                    roundDifficulty = Difficulty.EASY;
                    break;
                case 2:
                    roundDifficulty = Difficulty.MEDIUM;
                    break;
                case 3:
                    roundDifficulty = Difficulty.HARD;
                    break;
                default:
                    System.out.println("Invalid difficulty!");
                    return;
            }

            List<TopicTag> topicTags = new ArrayList<>();

            System.out.print("How many topics were discussed? ");
            int numberOfTopics = scanner.nextInt();
            scanner.nextLine();

            for (int j = 1; j <= numberOfTopics; j++) {

                System.out.print("Enter topic " + j + ": ");
                String tagName = scanner.nextLine();

                TopicTag tag = new TopicTag(
                        DataStore.nextTagId(),
                        tagName);

                topicTags.add(tag);
                DataStore.topicTags.add(tag);
            }

            RoundDetail round = new RoundDetail(
                    i,
                    roundType,
                    mode,
                    duration,
                    questions,
                    roundDifficulty,
                    topicTags);

            rounds.add(round);
        }

        System.out.println("All interview rounds added successfully!");

        InterviewExperience experience = new InterviewExperience(
                0,
                difficulty,
                tips,
                anonymous,
                java.time.LocalDate.now(),
                rounds);

        ExperienceService service = new ExperienceService();

        service.logExperience(application, experience);
    }
}
