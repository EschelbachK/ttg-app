package com.traintogain.backend.exercise.dto;

import com.traintogain.backend.common.BodyRegion;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class UpdateTrainingExerciseRequest {

    @NotBlank
    private String name;

    private String exerciseId;

    private BodyRegion bodyRegion;
    private String notes;

    @Min(0)
    private Integer restTimerSeconds;

    private List<@Valid SetEntryRequest> sets;

    public String getName() {
        return name;
    }

    public String getExerciseId() {
        return exerciseId;
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