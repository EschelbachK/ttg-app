package com.traintogain.backend.workout.service;

import com.traintogain.backend.exception.NotFoundException;
import com.traintogain.backend.workout.domain.*;
import com.traintogain.backend.workout.dto.*;
import com.traintogain.backend.workout.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutSessionService {

    private final WorkoutSessionRepository repository;
    private final WorkoutExerciseResolver resolver;

    public WorkoutSession startWorkout(String userId) {
        return repository.findFirstByUserIdAndFinishedAtIsNull(userId)
                .orElseGet(() -> repository.save(
                        WorkoutSession.builder()
                                .userId(userId)
                                .startedAt(Instant.now())
                                .exercises(new ArrayList<>())
                                .build()
                ));
    }

    public WorkoutSession startWorkoutWithPlan(StartWorkoutWithPlanRequest request) {

        WorkoutSession session = WorkoutSession.builder()
                .userId(request.getUserId())
                .startedAt(Instant.now())
                .exercises(new ArrayList<>())
                .build();

        int order = 0;

        for (var ex : request.getExercises()) {
            session.getExercises().add(
                    ExerciseSession.create(
                            ex.getExerciseId(),
                            resolver.resolveName(ex.getExerciseId(), ex.getName()),
                            order++
                    )
            );
        }

        return repository.save(session);
    }

    public WorkoutSession getActiveWorkout(String userId) {
        return repository.findFirstByUserIdAndFinishedAtIsNull(userId)
                .orElseThrow(() -> new NotFoundException("No active workout"));
    }

    public WorkoutSession finishWorkout(String userId) {
        WorkoutSession session = getActiveWorkout(userId);

        if (session.getExercises().isEmpty()) throw new IllegalArgumentException("Workout is empty");

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
                            resolver.resolveName(request.getExerciseId(), request.getName()),
                            session.getExercises().size()
                    );
                    session.getExercises().add(ex);
                    return ex;
                });

        SetLog set = SetLog.create(request.getWeight(), request.getReps());

        exercise.getSets().add(set);

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
            final int index = i;
            String id = request.getExerciseIds().get(i);

            session.getExercises().stream()
                    .filter(e -> e.getId().equals(id))
                    .findFirst()
                    .ifPresent(e -> e.setOrder(index));
        }

        return repository.save(session);
    }

    public WorkoutSession startRest(String userId, String setId) {
        WorkoutSession session = getActiveWorkout(userId);

        session.getExercises().forEach(ex ->
                ex.getSets().forEach(set -> {
                    if (set.getId().equals(setId)) {
                        set.setRestStartedAt(Instant.now());
                    }
                })
        );

        return repository.save(session);
    }

    public WorkoutSession finishRest(String userId, String setId) {
        WorkoutSession session = getActiveWorkout(userId);

        session.getExercises().forEach(ex ->
                ex.getSets().forEach(set -> {
                    if (set.getId().equals(setId) && set.getRestStartedAt() != null) {
                        int seconds = (int) (Instant.now().getEpochSecond() - set.getRestStartedAt().getEpochSecond());
                        set.setRestSeconds(seconds);
                    }
                })
        );

        return repository.save(session);
    }
}