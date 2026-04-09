package com.traintogain.backend.workout.service;

import com.traintogain.backend.workout.domain.*;
import com.traintogain.backend.workout.dto.AddSetRequest;
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
                .exercises(new java.util.ArrayList<>())
                .build();

        return repository.save(session);
    }

    public WorkoutSession finishWorkout(String userId) {
        WorkoutSession session = getActiveSession(userId);
        session.setFinishedAt(Instant.now());
        return repository.save(session);
    }

    public List<WorkoutSession> getWorkouts(String userId) {
        return repository.findByUserId(userId);
    }

    public WorkoutSession addSet(String userId, AddSetRequest request) {
        WorkoutSession session = getActiveSession(userId);

        ExerciseSession exercise = session.getExercises()
                .stream()
                .filter(e -> e.getExerciseId().equals(request.getExerciseId()))
                .findFirst()
                .orElseGet(() -> {
                    ExerciseSession ex = ExerciseSession.create(
                            request.getExerciseId(),
                            request.getName(),
                            session.getExercises().size()
                    );
                    session.getExercises().add(ex);
                    return ex;
                });

        exercise.getSets().add(SetLog.create(request.getWeight(), request.getReps()));

        return repository.save(session);
    }

    private WorkoutSession getActiveSession(String userId) {
        return repository.findFirstByUserIdAndFinishedAtIsNull(userId)
                .orElseThrow();
    }
}