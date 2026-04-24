package com.traintogain.backend.exercise;

import org.springframework.stereotype.Service;

@Service
public class ExerciseValidationService {

    public void validateFamily(String family) {
        if (family == null) {
            throw new IllegalStateException("Family is null");
        }

        try {
            ExerciseFamily.valueOf(family);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid family: " + family);
        }
    }

    public void validateBasePattern(String basePattern) {
        if (basePattern == null) {
            throw new IllegalStateException("BasePattern is null");
        }

        try {
            BasePatternRegistry.valueOf(basePattern);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid base pattern: " + basePattern);
        }
    }

    public void validateExercise(String family, String basePattern) {
        validateFamily(family);
        validateBasePattern(basePattern);
    }
}