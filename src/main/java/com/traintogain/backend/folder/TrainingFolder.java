package com.traintogain.backend.folder;

import com.traintogain.backend.common.BodyRegion;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "training_folders")
public class TrainingFolder {

    @Id
    private String id;

    private String userId;

    private String trainingPlanId;
    private String name;
    private BodyRegion bodyRegion;
    private int order;

    public TrainingFolder() {
    }

    public TrainingFolder(String trainingPlanId, String name, BodyRegion bodyRegion, int order) {
        this.trainingPlanId = trainingPlanId;
        this.name = name;
        this.bodyRegion = bodyRegion;
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTrainingPlanId() {
        return trainingPlanId;
    }

    public String getName() {
        return name;
    }

    public BodyRegion getBodyRegion() {
        return bodyRegion;
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

    public void setTrainingPlanId(String trainingPlanId) {
        this.trainingPlanId = trainingPlanId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBodyRegion(BodyRegion bodyRegion) {
        this.bodyRegion = bodyRegion;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}