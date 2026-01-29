package com.traintogain.backend.training;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "training_plans")
public class TrainingPlan {

    @Id
    private String id;

    private String userId;     // Besitzer
    private String title;      // z.B. "Tag 1 - Push"
    private boolean archived;  // Archiv / Aktiv

    public TrainingPlan() {
    }

    public TrainingPlan(String userId, String title) {
        this.userId = userId;
        this.title = title;
        this.archived = false;
    }

    // Getter & Setter

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
}
