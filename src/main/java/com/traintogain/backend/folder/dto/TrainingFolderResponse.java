package com.traintogain.backend.folder.dto;

import com.traintogain.backend.folder.TrainingFolder;
import lombok.Getter;

@Getter
public class TrainingFolderResponse {

    private String id;
    private String name;
    private String trainingPlanId;

    /// 🔥 WICHTIG für Frontend (rotes Label)
    private String trainingPlanName;

    private int order;

    public TrainingFolderResponse() {}

    public TrainingFolderResponse(
            String id,
            String name,
            String trainingPlanId,
            String trainingPlanName,
            int order
    ) {
        this.id = id;
        this.name = name;
        this.trainingPlanId = trainingPlanId;
        this.trainingPlanName = trainingPlanName;
        this.order = order;
    }

    public static TrainingFolderResponse fromEntity(
            TrainingFolder folder,
            String planName
    ) {
        return new TrainingFolderResponse(
                folder.getId(),
                folder.getName(),
                folder.getTrainingPlanId(),
                planName,
                folder.getOrder()
        );
    }
}