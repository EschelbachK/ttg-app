package com.traintogain.backend.training.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTrainingPlanRequest(

        @NotBlank(message = "Titel darf nicht leer sein")
        String title

) {}