package com.traintogain.backend.exercise.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateTrainingExerciseRequest(

        @JsonAlias("catalogId")
        @NotBlank(message = "ExerciseId darf nicht leer sein!")
        String exerciseId,

        List<@Valid SetEntryRequest> sets
) {}