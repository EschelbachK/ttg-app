package com.traintogain.backend.folder.dto;

import com.traintogain.backend.common.BodyRegion;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTrainingFolderRequest(

        @NotBlank(message = "TrainingPlanId darf nicht leer sein")
        String trainingPlanId,

        @NotBlank(message = "Name darf nicht leer sein")
        String name,

        @NotNull(message = "Körperbereich darf nicht null sein")
        BodyRegion bodyRegion,

        @Min(value = 0, message = "Reihenfolge muss >= 0 sein")
        int order

) {}