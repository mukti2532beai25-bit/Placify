
package com.placement.util;

import com.placement.model.InterviewExperience;
import com.placement.model.RoundDetail;
import com.placement.model.TopicTag;

import java.io.*;
import java.util.List;

public class FileHandler {

    // Export interview experiences to a CSV file.
    public static void exportToCSV(
            List<InterviewExperience> experiences,
            String fileName) throws IOException {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(fileName))) {

            writer.write("ExperienceID,ApplicationID,Difficulty,Tips,Anonymous,DatePosted");
            writer.newLine();

            if (experiences == null) {
                return;
            }

            for (InterviewExperience experience : experiences) {
                if (experience == null) {
                    continue;
                }

                String row =
                        experience.getExperienceId() + "," +
                        experience.getApplicationId() + "," +
                        experience.getDifficulty() + "," +
                        escapeCSV(experience.getTips()) + "," +
                        experience.isAnonymous() + "," +
                        experience.getDatePosted();

                writer.write(row);
                writer.newLine();
            }
        }
    }

    // Escape commas, quotes, and newlines in CSV values.
    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }

        value = value.replace("\"", "\"\"");

        if (value.contains(",") || value.contains("\"")
                || value.contains("\n") || value.contains("\r")) {
            return "\"" + value + "\"";
        }

        return value;
    }

    // Create a binary backup of all interview experiences.
    public static void backupData(
            List<InterviewExperience> experiences,
            String fileName) throws IOException {

        try (ObjectOutputStream output =
                     new ObjectOutputStream(new FileOutputStream(fileName))) {

            output.writeObject(experiences);
        }
    }

    // Restore interview experiences from a binary backup.
    @SuppressWarnings("unchecked")
    public static List<InterviewExperience> restoreData(
            String fileName) throws IOException, ClassNotFoundException {

        try (ObjectInputStream input =
                     new ObjectInputStream(new FileInputStream(fileName))) {

            return (List<InterviewExperience>) input.readObject();
        }
    }
}
