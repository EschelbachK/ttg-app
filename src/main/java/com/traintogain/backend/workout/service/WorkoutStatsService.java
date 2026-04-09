package com.traintogain.backend.workout.service;

import com.traintogain.backend.exception.NotFoundException;
import com.traintogain.backend.workout.domain.WorkoutSession;
import com.traintogain.backend.workout.dto.WorkoutSummaryResponse;
import com.traintogain.backend.workout.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkoutStatsService {

    private final WorkoutSessionRepository repository;

    public WorkoutSummaryResponse getSummary(String sessionId) {

        WorkoutSession session = repository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Workout not found"));

        long duration = session.getFinishedAt().getEpochSecond() - session.getStartedAt().getEpochSecond();

        double volume = session.getExercises().stream()
                .flatMap(e -> e.getSets().stream())
                .mapToDouble(s -> s.getWeight() * s.getReps())
                .sum();

        int sets = session.getExercises().stream()
                .mapToInt(e -> e.getSets().size())
                .sum();

        return WorkoutSummaryResponse.builder()
                .durationSeconds(duration)
                .totalVolume(volume)
                .totalSets(sets)
                .build();
    }
}