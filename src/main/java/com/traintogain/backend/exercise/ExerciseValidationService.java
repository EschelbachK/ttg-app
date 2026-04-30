package com.traintogain.backend.exercise;

import org.springframework.stereotype.Service;

@Service
public class ExerciseValidationService {

    public void validateExercise(ExerciseFamily family, BaseMovementPattern basePattern) {
        if (family == null || basePattern == null) {
            throw new IllegalArgumentException("invalid exercise metadata");
        }
    }
}