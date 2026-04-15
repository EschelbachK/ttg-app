package com.traintogain.backend.training;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "training_plans")
public class TrainingPlan {

    @Id
    private String id;

    private String userId;

    private String title;

    private boolean archived = false;

    private int order = 0;

    public TrainingPlan() {
    }

    public TrainingPlan(String userId, String title, int order) {
        this.userId = userId;
        this.title = title;
        this.order = order;
        this.archived = false;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public boolean isArchived() {
        return archived;
    }

    public int getOrder() {
        return order;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}