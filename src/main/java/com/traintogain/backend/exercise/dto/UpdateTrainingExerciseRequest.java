package com.traintogain.backend.exercise.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateTrainingExerciseRequest {

    @JsonAlias("catalogId")
    private String exerciseId;

    private String notes;

    @Min(0)
    private Integer restTimerSeconds;

    private List<@Valid SetEntryRequest> sets;
}