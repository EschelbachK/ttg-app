package com.traintogain.backend.workout.service;

import com.traintogain.backend.workout.domain.WorkoutSession;
import com.traintogain.backend.workout.dto.WorkoutSuggestionResponse;
import com.traintogain.backend.workout.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class WorkoutSuggestionService {

    private final WorkoutSessionRepository repository;

    public WorkoutSuggestionResponse getSuggestion(String userId, String exerciseId) {

        WorkoutSession last = repository.findByUserId(userId).stream()
                .filter(s -> s.getFinishedAt() != null)
                .sorted(Comparator.comparing(WorkoutSession::getFinishedAt).reversed())
                .findFirst()
                .orElse(null);

        if (last == null) return WorkoutSuggestionResponse.builder().suggestedWeight(0).suggestedReps(0).build();

        var exercise = last.getExercises().stream()
                .filter(e -> e.getExerciseId().equals(exerciseId))
                .findFirst()
                .orElse(null);

        if (exercise == null || exercise.getSets().isEmpty())
            return WorkoutSuggestionResponse.builder().suggestedWeight(0).suggestedReps(0).build();

        var lastSet = exercise.getSets().get(exercise.getSets().size() - 1);

        return WorkoutSuggestionResponse.builder()
                .suggestedWeight(lastSet.getWeight())
                .suggestedReps(lastSet.getReps())
                .build();
    }
}