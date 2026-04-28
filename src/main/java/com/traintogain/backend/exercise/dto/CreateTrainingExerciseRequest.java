package com.traintogain.backend.exercise.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * DTO für das Anlegen einer TrainingExercise.
 */
public record CreateTrainingExerciseRequest(

        @NotBlank(message = "ExerciseId darf nicht leer sein!")
        String exerciseId,

        List<@Valid SetEntryRequest> sets
) {}