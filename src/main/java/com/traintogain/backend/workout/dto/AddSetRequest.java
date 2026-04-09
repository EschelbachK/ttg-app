package com.traintogain.backend.workout.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddSetRequest {
    private String exerciseId;
    private String name;
    private double weight;
    private int reps;
}