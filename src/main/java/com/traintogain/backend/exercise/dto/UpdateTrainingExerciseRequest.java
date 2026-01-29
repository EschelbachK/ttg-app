package com.traintogain.backend.exercise.dto;

import com.traintogain.backend.common.BodyRegion;

import java.util.List;

public class UpdateTrainingExerciseRequest {

    private String name;
    private BodyRegion bodyRegion;
    private String notes;
    private Integer restTimerSeconds;
    private List<SetEntryRequest> sets;

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
