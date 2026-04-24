package com.traintogain.backend.exercise;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class SmartAlternativeRankingEngine {

    private final ExerciseReferenceResolver resolver;

    public SmartAlternativeRankingEngine(ExerciseReferenceResolver resolver) {
        this.resolver = resolver;
    }

    public List<RankedExercise> rank(String exerciseId, List<TrainingExercise> candidates) {

        ExerciseReference base = resolver.resolve(exerciseId);

        List<RankedExercise> result = new ArrayList<>();

        for (TrainingExercise ex : candidates) {

            if (ex.getExerciseId() == null || ex.getExerciseId().equals(exerciseId)) {
                continue;
            }

            ExerciseReference other = resolver.resolve(ex.getExerciseId());

            int score = 0;

            if (other.getFamily() == base.getFamily()) {
                score += 40;
            }

            if (other.getBasePattern() == base.getBasePattern()) {
                score += 25;
            }

            if (isSameMuscle(other.getPrimaryMuscle(), base.getPrimaryMuscle())) {
                score += 15;
            }

            score += equipmentScore(ex, base);

            score += difficultyScore(other);

            result.add(new RankedExercise(ex, score));
        }

        result.sort(Comparator.comparingInt(RankedExercise::score).reversed());

        return result;
    }

    private int equipmentScore(TrainingExercise ex, ExerciseReference base) {

        if (ex.getName() == null) return 0;

        String name = ex.getName().toLowerCase();

        if (name.contains("dumbbell") && base.getFamily() != null) {
            return 10;
        }

        if (name.contains("machine")) {
            return 5;
        }

        if (name.contains("barbell")) {
            return 8;
        }

        return 0;
    }

    private int difficultyScore(ExerciseReference ref) {

        if (ref.getFamily() == null) return 0;

        String fam = ref.getFamily().name();

        if (fam.contains("BENCH") || fam.contains("ROW")) {
            return 5;
        }

        return 0;
    }

    private boolean isSameMuscle(String a, String b) {
        if (a == null || b == null) return false;
        return a.equalsIgnoreCase(b);
    }
}