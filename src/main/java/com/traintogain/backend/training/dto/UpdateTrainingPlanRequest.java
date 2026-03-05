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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }
}