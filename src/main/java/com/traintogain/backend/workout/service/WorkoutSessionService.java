package com.traintogain.backend.workout.service;

import com.traintogain.backend.workout.model.WorkoutSession;
import com.traintogain.backend.workout.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutSessionService {

    private final WorkoutSessionRepository repository;

    public WorkoutSession startWorkout(String userId) {

        WorkoutSession session = WorkoutSession.builder()
                .userId(userId)
                .startedAt(Instant.now())
                .build();

        return repository.save(session);
    }

    public WorkoutSession saveWorkout(WorkoutSession session) {
        session.setFinishedAt(Instant.now());
        return repository.save(session);
    }

    public List<WorkoutSession> getUserWorkouts(String userId) {
        return repository.findByUserId(userId);
    }

}