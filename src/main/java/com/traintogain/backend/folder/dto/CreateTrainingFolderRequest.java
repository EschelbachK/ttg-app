package com.traintogain.backend.folder.dto;

import com.traintogain.backend.common.BodyRegion;

public record CreateTrainingFolderRequest(
        String trainingPlanId,
        String name,
        BodyRegion bodyRegion,
        int order
) {}