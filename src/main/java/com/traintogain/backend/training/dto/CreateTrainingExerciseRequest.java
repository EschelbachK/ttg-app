package com.traintogain.backend.training.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateTrainingExerciseRequest(

        @NotBlank
        String folderId,

        @NotBlank
        String name,

        @NotEmpty
        List<CreateSetRequest> sets

) {}