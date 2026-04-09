package com.traintogain.backend.workout.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseResolver {

    public String resolveName(String exerciseId, String fallback) {
        if (fallback != null && !fallback.isBlank()) return fallback;
        return "Exercise";
    }
}