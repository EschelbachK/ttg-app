package com.traintogain.backend.workout.service;

import com.traintogain.backend.workout.domain.WorkoutSession;
import com.traintogain.backend.workout.dto.WorkoutHistoryResponse;
import com.traintogain.backend.workout.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class WorkoutHistoryService {

    private final WorkoutSessionRepository repository;

    public WorkoutHistoryResponse getHistory(String userId, String exerciseId) {

        WorkoutSession last = repository.findByUserId(userId).stream()
                .filter(s -> s.getFinishedAt() != null)
                .sorted(Comparator.comparing(WorkoutSession::getFinishedAt).reversed())
                .findFirst()
                .orElse(null);

        if (last == null) return WorkoutHistoryResponse.builder().exerciseId(exerciseId).lastSets(java.util.List.of()).build();

        var exercise = last.getExercises().stream()
                .filter(e -> e.getExerciseId().equals(exerciseId))
                .findFirst()
                .orElse(null);

        if (exercise == null) return WorkoutHistoryResponse.builder().exerciseId(exerciseId).lastSets(java.util.List.of()).build();

        var sets = exercise.getSets().stream()
                .map(s -> WorkoutHistoryResponse.SetData.builder()
                        .weight(s.getWeight())
                        .reps(s.getReps())
                        .build())
                .toList();

        return WorkoutHistoryResponse.builder()
                .exerciseId(exerciseId)
                .lastSets(sets)
                .build();
    }
}