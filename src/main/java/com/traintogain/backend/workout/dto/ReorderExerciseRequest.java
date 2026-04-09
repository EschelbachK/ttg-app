package com.traintogain.backend.workout.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReorderExerciseRequest {

    private List<String> exerciseIds;
}