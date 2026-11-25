
package com.placement.service;

import com.placement.enums.ApplicationStage;
import com.placement.model.Application;
import com.placement.model.Drive;
import com.placement.model.InterviewExperience;
import com.placement.model.RoundDetail;
import com.placement.model.TopicTag;
import com.placement.repository.DataStore;

import java.util.List;
import java.util.TreeMap;

public class ExperienceService {

    // An experience can be submitted only after results are declared.
    public boolean canSubmitExperience(Application application) {
        return application != null
                && application.getStage() == ApplicationStage.RESULT_DECLARED;
    }

    // Round numbers must be sequential: 1, 2, 3, and so on.
    public boolean validateRounds(List<RoundDetail> rounds) {
        if (rounds == null || rounds.isEmpty()) {
            return false;
        }

        for (int i = 0; i < rounds.size(); i++) {
            RoundDetail round = rounds.get(i);

            if (round == null || round.getRoundNumber() != i + 1) {
                return false;
            }
        }

        return true;
    }

    // Save a new interview experience.
    public boolean logExperience(Application application,
                                 InterviewExperience experience) {

        if (!canSubmitExperience(application)) {
            System.out.println("You can submit an experience only after results are declared.");
            return false;
        }

        if (experience == null || !validateRounds(experience.getRounds())) {
            System.out.println("Invalid interview experience or round details.");
            return false;
        }

        experience.setExperienceId(DataStore.nextExperienceId());
        experience.setApplicationId(application.getApplicationId());

        DataStore.experiences.add(experience);

        System.out.println("Interview experience saved successfully.");
        return true;
    }

    // Search experiences using words in their tips.
    public String searchArchive(List<InterviewExperience> experiences,
                                String keyword) {

        if (experiences == null || keyword == null || keyword.trim().isEmpty()) {
            return "No matching experiences found.";
        }

        StringBuilder result = new StringBuilder();

        for (InterviewExperience experience : experiences) {
            if (experience == null || experience.getTips() == null) {
                continue;
            }

            if (experience.getTips().toLowerCase()
                    .contains(keyword.trim().toLowerCase())) {

                result.append("Experience ID: ")
                        .append(experience.getExperienceId())
                        .append("\nTips: ")
                        .append(experience.getTips())
                        .append("\nDifficulty: ")
                        .append(experience.getDifficulty())
                        .append("\n\n");
            }
        }

        if (result.length() == 0) {
            return "No matching experiences found.";
        }

        return result.toString();
    }

    // Count how frequently each topic appears.
    public TreeMap<String, Integer> countTopicFrequencies(
            List<InterviewExperience> experiences) {

        TreeMap<String, Integer> topicCounts = new TreeMap<>();

        if (experiences == null) {
            return topicCounts;
        }

        for (InterviewExperience experience : experiences) {
            if (experience == null || experience.getRounds() == null) {
                continue;
            }

            for (RoundDetail round : experience.getRounds()) {
                if (round == null || round.getTopicTags() == null) {
                    continue;
                }

                for (TopicTag tag : round.getTopicTags()) {
                    if (tag == null || tag.getTagName() == null) {
                        continue;
                    }

                    String topic = tag.getTagName();

                    topicCounts.put(topic,
                            topicCounts.getOrDefault(topic, 0) + 1);
                }
            }
        }

        return topicCounts;
    }

    // Search experiences by the company where the student interviewed.
    public String searchByCompany(
            List<InterviewExperience> experiences,
            List<Application> applications,
            List<Drive> drives,
            String companyName) {

        if (experiences == null || applications == null
                || drives == null || companyName == null) {
            return "No matching experiences found.";
        }

        StringBuilder result = new StringBuilder();

        for (InterviewExperience experience : experiences) {
            if (experience == null) {
                continue;
            }

            for (Application application : applications) {
                if (application == null
                        || application.getApplicationId()
                        != experience.getApplicationId()) {
                    continue;
                }

                for (Drive drive : drives) {
                    if (drive == null
                            || drive.getDriveId() != application.getDriveId()
                            || drive.getCompany() == null) {
                        continue;
                    }

                    if (drive.getCompany().getName()
                            .equalsIgnoreCase(companyName.trim())) {

                        result.append("Experience ID: ")
                                .append(experience.getExperienceId())
                                .append("\nCompany: ")
                                .append(drive.getCompany().getName())
                                .append("\nTips: ")
                                .append(experience.getTips())
                                .append("\n\n");
                    }
                }
            }
        }

        return result.length() == 0
                ? "No matching experiences found."
                : result.toString();
    }

    // Search experiences by a topic such as DSA, SQL, or DBMS.
    public String searchByTopic(
            List<InterviewExperience> experiences, String topicName) {

        if (experiences == null || topicName == null
                || topicName.trim().isEmpty()) {
            return "No matching experiences found.";
        }

        StringBuilder result = new StringBuilder();

        for (InterviewExperience experience : experiences) {
            if (experience == null || experience.getRounds() == null) {
                continue;
            }

            boolean matched = false;

            for (RoundDetail round : experience.getRounds()) {
                if (round == null || round.getTopicTags() == null) {
                    continue;
                }

                for (TopicTag tag : round.getTopicTags()) {
                    if (tag != null && tag.getTagName() != null
                            && tag.getTagName().equalsIgnoreCase(topicName.trim())) {
                        matched = true;
                        break;
                    }
                }

                if (matched) {
                    break;
                }
            }

            if (matched) {
                result.append("Experience ID: ")
                        .append(experience.getExperienceId())
                        .append("\nTopic: ")
                        .append(topicName)
                        .append("\nDifficulty: ")
                        .append(experience.getDifficulty())
                        .append("\n\n");
            }
        }

        return result.length() == 0
                ? "No matching experiences found."
                : result.toString();
    }
}
