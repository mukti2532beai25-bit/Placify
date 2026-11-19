
package com.placement.util;

import java.io.*;
import java.util.List;

import com.placement.model.InterviewExperience;
import com.placement.model.RoundDetail;
import com.placement.model.TopicTag;

public class FileHandler {

    // Export experiences into a CSV file
    public static void exportToCSV(
            List<InterviewExperience> experiences,
            String fileName) throws IOException {

        BufferedWriter writer =
                new BufferedWriter(new FileWriter(fileName));

        writer.write("ExperienceID,Difficulty,Tips,Anonymous,DatePosted");
        writer.newLine();

        for (InterviewExperience experience : experiences) {

            writer.write(
                    experience.getExperienceId() + "," +
                    experience.getDifficulty() + "," +
                    experience.getTips() + "," +
                    experience.isAnonymous() + "," +
                    experience.getDatePosted()
            );

            writer.newLine();
        }

        writer.close();
    }

    // Save experiences into a binary file
    public static void backupData(
            List<InterviewExperience> experiences,
            String fileName) throws IOException {

        ObjectOutputStream output =
                new ObjectOutputStream(
                        new FileOutputStream(fileName));

        output.writeObject(experiences);
        output.close();
    }

    // Restore experiences from a binary file
    @SuppressWarnings("unchecked")
    public static List<InterviewExperience> restoreData(
            String fileName)
            throws IOException, ClassNotFoundException {

        ObjectInputStream input =
                new ObjectInputStream(
                        new FileInputStream(fileName));

        List<InterviewExperience> experiences =
                (List<InterviewExperience>) input.readObject();

        input.close();

        return experiences;
    }
}
