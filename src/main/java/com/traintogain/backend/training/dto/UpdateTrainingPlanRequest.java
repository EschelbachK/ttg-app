package com.traintogain.backend.training.dto;

public class UpdateTrainingPlanRequest {

    private String title;
    private Boolean archived;

    public String getTitle() {
        return title;
    }

    public Boolean getArchived() {
        return archived;
    }
}