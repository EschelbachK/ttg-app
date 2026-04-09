package com.traintogain.backend.workout.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSetRequest {

    private String exerciseId;
    private String setId;
    private double weight;
    private int reps;
}