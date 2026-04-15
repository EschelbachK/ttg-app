package com.traintogain.backend.exercise.dto;

import com.traintogain.backend.common.BodyRegion;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class UpdateTrainingExerciseRequest {

    @NotBlank(message = "Übungsname darf nicht leer sein")
    private String name;

    private BodyRegion bodyRegion;
    private String notes;

    @Min(value = 0, message = "Pause muss >= 0 Sekunden sein")
    private Integer restTimerSeconds;

    private List<@Valid SetEntryRequest> sets;

    public String getName() {
        return name;
    }

    public BodyRegion getBodyRegion() {
        return bodyRegion;
    }

    public String getNotes() {
        return notes;
    }

    public Integer getRestTimerSeconds() {
        return restTimerSeconds;
    }

    public List<SetEntryRequest> getSets() {
        return sets;
    }
}