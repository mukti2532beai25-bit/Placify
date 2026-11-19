
package com.placement.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.placement.enums.Difficulty;

public class InterviewExperience implements Serializable {

    private static final long serialVersionUID = 1L;

    private int experienceId;
    private Difficulty difficulty;
    private String tips;
    private boolean isAnonymous;
    private LocalDate datePosted;
    private List<RoundDetail> rounds;

    public InterviewExperience(int experienceId,
                               Difficulty difficulty,
                               String tips,
                               boolean isAnonymous,
                               LocalDate datePosted,
                               List<RoundDetail> rounds) {

        this.experienceId = experienceId;
        this.difficulty = difficulty;
        this.tips = tips;
        this.isAnonymous = isAnonymous;
        this.datePosted = datePosted;
        this.rounds = rounds;
    }

    public int getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(int experienceId) {
        this.experienceId = experienceId;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public LocalDate getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(LocalDate datePosted) {
        this.datePosted = datePosted;
    }

    public List<RoundDetail> getRounds() {
        return rounds;
    }

    public void setRounds(List<RoundDetail> rounds) {
        this.rounds = rounds;
    }
}
