package com.traintogain.backend.exercise.dto;

import com.traintogain.backend.common.BodyRegion;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateTrainingExerciseRequest(
        @NotBlank(message = "Übungsname darf nicht leer sein!")
        String name,

        @NotBlank(message = "ExerciseId darf nicht leer sein!")
        String exerciseId,

        BodyRegion bodyRegion,

        List<@Valid SetEntryRequest> sets
) {}