package com.traintogain.backend.workout.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class StartWorkoutWithPlanRequest {

    private String userId;
    private List<PlanExercise> exercises;

    @Getter
    @Setter
    public static class PlanExercise {
        private String exerciseId;
        private String name;
    }
}