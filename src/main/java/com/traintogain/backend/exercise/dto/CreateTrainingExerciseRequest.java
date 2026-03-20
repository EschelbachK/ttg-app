package com.traintogain.backend.exercise.dto;

import com.traintogain.backend.common.BodyRegion;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateTrainingExerciseRequest(

        @NotBlank(message = "Ordner-ID darf nicht leer sein")
        String folderId,

        @NotBlank(message = "Übungsname darf nicht leer sein")
        String name,

        BodyRegion bodyRegion,

        @NotEmpty(message = "Es muss mindestens ein Satz vorhanden sein")
        List<@Valid SetEntryRequest> sets

) {}