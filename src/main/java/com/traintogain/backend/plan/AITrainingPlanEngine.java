package com.traintogain.backend.plan;

import com.traintogain.backend.exercise.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AITrainingPlanEngine {

    private final ExerciseReferenceResolver resolver;
    private final ExerciseAlternativesService alternativesService;

    public AITrainingPlanEngine(
            ExerciseReferenceResolver resolver,
            ExerciseAlternativesService alternativesService
    ) {
        this.resolver = resolver;
        this.alternativesService = alternativesService;
    }

    public OptimizedTrainingPlan optimize(List<TrainingExercise> planExercises) {

        List<TrainingExercise> optimized = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        int chestVolume = 0;
        int backVolume = 0;
        int legVolume = 0;

        for (TrainingExercise ex : planExercises) {

            ExerciseReference ref = resolver.resolve(ex.getExerciseId());

            switch (ref.getFamily()) {
                case BENCH_PRESS -> chestVolume++;
                case ROW -> backVolume++;
                case SQUAT -> legVolume++;
            }

            optimized.add(ex);
        }

        if (chestVolume > backVolume + 2) {
            warnings.add("Chest volume too high vs back");
        }

        if (legVolume < 2) {
            warnings.add("Leg volume too low");
        }

        return new OptimizedTrainingPlan(optimized, warnings);
    }

    public TrainingExercise replaceIfNeeded(
            TrainingExercise exercise,
            boolean hasEquipment
    ) {

        if (hasEquipment) {
            return exercise;
        }

        List<TrainingExercise> alternatives =
                alternativesService.getAlternatives(exercise.getExerciseId());

        return alternatives.isEmpty()
                ? exercise
                : alternatives.get(0);
    }
}