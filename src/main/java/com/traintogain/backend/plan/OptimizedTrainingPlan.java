package com.traintogain.backend.plan;

import com.traintogain.backend.exercise.TrainingExercise;

import java.util.List;

public class OptimizedTrainingPlan {

    private final List<TrainingExercise> exercises;
    private final List<String> warnings;

    public OptimizedTrainingPlan(List<TrainingExercise> exercises, List<String> warnings) {
        this.exercises = exercises;
        this.warnings = warnings;
    }

    public List<TrainingExercise> getExercises() {
        return exercises;
    }

    public List<String> getWarnings() {
        return warnings;
    }
}