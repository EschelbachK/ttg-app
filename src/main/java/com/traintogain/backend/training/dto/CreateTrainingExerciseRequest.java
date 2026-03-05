package com.traintogain.backend.training.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateTrainingExerciseRequest(

        @NotBlank(message = "FolderId must not be blank")
        String folderId,

        @NotBlank(message = "Exercise name must not be blank")
        String name,

        @NotEmpty(message = "Exercise must contain at least one set")
        List<@Valid CreateSetRequest> sets

) {}