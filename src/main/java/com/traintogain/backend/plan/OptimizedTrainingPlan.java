package com.traintogain.backend.plan;

import com.traintogain.backend.exercise.TrainingExercise;

import java.util.List;

public record OptimizedTrainingPlan(List<TrainingExercise> exercises, List<String> warnings) {

}