
package com.placement.model;

import java.io.Serializable;
import java.util.List;
import com.placement.enums.Difficulty;
import com.placement.enums.RoundType;

public class RoundDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private int roundNumber;
    private RoundType roundType;
    private String mode;
    private int durationMinutes;
    private String questionsAsked;
    private Difficulty difficulty;
    private List<TopicTag> topicTags;

    public RoundDetail(int roundNumber, RoundType roundType,
                       String mode, int durationMinutes,
                       String questionsAsked, Difficulty difficulty,
                       List<TopicTag> topicTags) {

        this.roundNumber = roundNumber;
        this.roundType = roundType;
        this.mode = mode;
        this.durationMinutes = durationMinutes;
        this.questionsAsked = questionsAsked;
        this.difficulty = difficulty;
        this.topicTags = topicTags;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public RoundType getRoundType() {
        return roundType;
    }

    public void setRoundType(RoundType roundType) {
        this.roundType = roundType;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getQuestionsAsked() {
        return questionsAsked;
    }

    public void setQuestionsAsked(String questionsAsked) {
        this.questionsAsked = questionsAsked;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<TopicTag> getTopicTags() {
        return topicTags;
    }

    public void setTopicTags(List<TopicTag> topicTags) {
        this.topicTags = topicTags;
    }
}
