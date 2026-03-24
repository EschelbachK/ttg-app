package com.traintogain.backend.workout.service;

import com.traintogain.backend.workout.model.ExerciseSession;
import com.traintogain.backend.workout.model.SetLog;
import com.traintogain.backend.workout.model.WorkoutSession;
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

    public WorkoutSession startWorkout(String userId) {
        WorkoutSession session = WorkoutSession.builder()
                .userId(userId)
                .startedAt(Instant.now())
                .exercises(new ArrayList<>())
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

    public List<SetLog> getSets(String userId, String exerciseId) {
        WorkoutSession session = getActiveSession(userId);

        for (ExerciseSession ex : session.getExercises()) {
            if (ex.getCatalogExerciseId().equals(exerciseId)) {
                return ex.getSets();
            }
        }

        return new ArrayList<>();
    }

    public List<SetLog> addSet(String userId, String exerciseId, SetLog set) {
        WorkoutSession session = getActiveSession(userId);

        ExerciseSession exercise = session.getExercises()
                .stream()
                .filter(e -> e.getCatalogExerciseId().equals(exerciseId))
                .findFirst()
                .orElseGet(() -> {
                    ExerciseSession newEx = ExerciseSession.builder()
                            .catalogExerciseId(exerciseId)
                            .name("")
                            .sets(new ArrayList<>())
                            .build();

                    session.getExercises().add(newEx);
                    return newEx;
                });

        exercise.getSets().add(set);

        repository.save(session);

        return exercise.getSets();
    }

    private WorkoutSession getActiveSession(String userId) {
        return repository.findByUserId(userId)
                .stream()
                .filter(s -> s.getFinishedAt() == null)
                .findFirst()
                .orElseThrow();
    }
}