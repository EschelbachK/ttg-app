package com.traintogain.backend.folder;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "training_folders")
public class TrainingFolder {

    @Id
    private String id;

    private String userId;
    private String trainingPlanId;
    private String name;
    private int order;
    private boolean archived;

    public TrainingFolder() {}

    public TrainingFolder(String trainingPlanId, String name, int order) {
        this.trainingPlanId = trainingPlanId;
        this.name = name;
        this.order = order;
        this.archived = false;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getTrainingPlanId() { return trainingPlanId; }
    public String getName() { return name; }
    public int getOrder() { return order; }
    public boolean isArchived() { return archived; }

    public void setId(String id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setTrainingPlanId(String trainingPlanId) { this.trainingPlanId = trainingPlanId; }
    public void setName(String name) { this.name = name; }
    public void setOrder(int order) { this.order = order; }
    public void setArchived(boolean archived) { this.archived = archived; }
}