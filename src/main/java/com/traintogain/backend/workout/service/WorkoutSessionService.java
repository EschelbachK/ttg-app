package com.traintogain.backend.workout.service;

import com.traintogain.backend.workout.domain.*;
import com.traintogain.backend.workout.dto.*;
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
        return repository.findFirstByUserIdAndFinishedAtIsNull(userId)
                .orElseGet(() -> repository.save(
                        WorkoutSession.builder()
                                .userId(userId)
                                .startedAt(Instant.now())
                                .exercises(new java.util.ArrayList<>())
                                .build()
                ));
    }

    public WorkoutSession getActiveWorkout(String userId) {
        return repository.findFirstByUserIdAndFinishedAtIsNull(userId)
                .orElseThrow();
    }

    public WorkoutSession finishWorkout(String userId) {
        WorkoutSession session = getActiveWorkout(userId);

        if (session.getExercises().isEmpty()) throw new RuntimeException();

        session.setFinishedAt(Instant.now());
        return repository.save(session);
    }

    public List<WorkoutSession> getWorkouts(String userId) {
        return repository.findByUserId(userId);
    }

    public WorkoutSession addSet(String userId, AddSetRequest request) {
        WorkoutSession session = getActiveWorkout(userId);

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

    public WorkoutSession updateSet(String userId, UpdateSetRequest request) {
        WorkoutSession session = getActiveWorkout(userId);

        session.getExercises().forEach(ex ->
                ex.getSets().forEach(set -> {
                    if (set.getId().equals(request.getSetId())) {
                        set.setWeight(request.getWeight());
                        set.setReps(request.getReps());
                    }
                })
        );

        return repository.save(session);
    }

    public WorkoutSession deleteSet(String userId, String setId) {
        WorkoutSession session = getActiveWorkout(userId);

        session.getExercises()
                .forEach(ex -> ex.getSets().removeIf(s -> s.getId().equals(setId)));

        return repository.save(session);
    }

    public WorkoutSession reorderExercises(String userId, ReorderExerciseRequest request) {
        WorkoutSession session = getActiveWorkout(userId);

        for (int i = 0; i < request.getExerciseIds().size(); i++) {
            String id = request.getExerciseIds().get(i);
            session.getExercises().stream()
                    .filter(e -> e.getId().equals(id))
                    .findFirst()
                    .ifPresent(e -> e.setOrder(i));
        }

        return repository.save(session);
    }
}