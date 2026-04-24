package com.traintogain.backend.exercise;

import org.springframework.stereotype.Component;

@Component
public class ExerciseReferenceResolver {

    public ExerciseReference resolve(String exerciseId) {

        ExerciseReference ref = new ExerciseReference();

        if (exerciseId == null || exerciseId.isBlank()) {
            throw new IllegalArgumentException("exerciseId fehlt");
        }

        if (isBenchPress(exerciseId)) {
            ref.setFamily(ExerciseFamily.BENCH_PRESS);
            ref.setBasePattern(BasePatternRegistry.HORIZONTAL_PUSH);
            ref.setPrimaryMuscle("CHEST");
        }

        else if (isInclinePress(exerciseId)) {
            ref.setFamily(ExerciseFamily.BENCH_PRESS);
            ref.setBasePattern(BasePatternRegistry.INCLINE_PUSH);
            ref.setPrimaryMuscle("UPPER_CHEST");
        }

        else if (isDeclinePress(exerciseId)) {
            ref.setFamily(ExerciseFamily.BENCH_PRESS);
            ref.setBasePattern(BasePatternRegistry.DECLINE_PUSH);
            ref.setPrimaryMuscle("LOWER_CHEST");
        }

        else if (isRow(exerciseId)) {
            ref.setFamily(ExerciseFamily.ROW);
            ref.setBasePattern(BasePatternRegistry.HORIZONTAL_PULL);
            ref.setPrimaryMuscle("BACK");
        }

        else if (isDeadlift(exerciseId)) {
            ref.setFamily(ExerciseFamily.DEADLIFT);
            ref.setBasePattern(BasePatternRegistry.HIP_HINGE);
            ref.setPrimaryMuscle("HAMSTRINGS");
        }

        else if (isSquat(exerciseId)) {
            ref.setFamily(ExerciseFamily.SQUAT);
            ref.setBasePattern(BasePatternRegistry.SQUAT_PATTERN);
            ref.setPrimaryMuscle("QUADS");
        }

        else {
            throw new IllegalArgumentException("Unknown exerciseId: " + exerciseId);
        }

        return ref;
    }

    private boolean isBenchPress(String id) {
        return id.contains("bench_press");
    }

    private boolean isInclinePress(String id) {
        return id.contains("incline");
    }

    private boolean isDeclinePress(String id) {
        return id.contains("decline");
    }

    private boolean isRow(String id) {
        return id.contains("row");
    }

    private boolean isDeadlift(String id) {
        return id.contains("deadlift");
    }

    private boolean isSquat(String id) {
        return id.contains("squat");
    }
}