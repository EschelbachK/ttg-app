package com.traintogain.backend.plan;

import com.traintogain.backend.catalog.model.ExerciseCatalog;
import com.traintogain.backend.catalog.service.ExerciseCatalogService;
import com.traintogain.backend.exercise.ExerciseAlternativesService;
import com.traintogain.backend.exercise.ExerciseFamily;
import com.traintogain.backend.exercise.TrainingExercise;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AITrainingPlanEngine {

    private final ExerciseCatalogService catalogService;
    private final ExerciseAlternativesService alternativesService;

    public AITrainingPlanEngine(
            ExerciseCatalogService catalogService,
            ExerciseAlternativesService alternativesService
    ) {
        this.catalogService = catalogService;
        this.alternativesService = alternativesService;
    }

    public OptimizedTrainingPlan optimize(List<TrainingExercise> planExercises) {

        List<TrainingExercise> optimized = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        int chestVolume = 0;
        int backVolume = 0;
        int legVolume = 0;

        for (TrainingExercise ex : planExercises) {

            ExerciseCatalog exercise;

            try {
                exercise = catalogService.getById(ex.getExerciseId());
            } catch (Exception ignored) {
                optimized.add(ex);
                continue;
            }

            ExerciseFamily family = exercise.getFamily();

            if (family == null) {
                optimized.add(ex);
                continue;
            }

            // --- Anpassung: Nur die Enums verwenden, die in der JSON existieren ---
            switch (family) {
                case PUSH -> chestVolume++; // Bankdrücken, Schulterdrücken, etc.
                case PULL -> backVolume++;  // Rudern, Klimmzüge
                case LEGS -> legVolume++;   // Squats, Deadlifts, Lunges
                case FULL_BODY, CORE, CONDITIONING -> {
                    // Ignorieren, kein Volumen-Counting nötig
                }
                default -> {
                    // Sicherheitsnetz, falls neue Enums hinzukommen
                }
            }

            optimized.add(ex);
        }

        // --- Warnungen auf Basis von Volumen ---
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

        if (hasEquipment) return exercise;

        List<TrainingExercise> alternatives =
                alternativesService.getAlternatives(exercise.getExerciseId());

        return alternatives.isEmpty() ? exercise : alternatives.get(0);
    }
}