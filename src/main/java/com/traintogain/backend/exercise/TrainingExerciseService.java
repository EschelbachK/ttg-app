package com.traintogain.backend.exercise;

import com.traintogain.backend.common.BodyRegion;
import com.traintogain.backend.exercise.dto.UpdateSetRequest;
import com.traintogain.backend.exercise.dto.UpdateTrainingExerciseRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TrainingExerciseService {

    private final TrainingExerciseRepository exerciseRepository;

    public TrainingExerciseService(TrainingExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public TrainingExercise addExercise(
            String folderId,
            String name,
            BodyRegion bodyRegion,
            List<SetEntry> sets
    ) {
        TrainingExercise exercise =
                new TrainingExercise(folderId, name, bodyRegion);

        exercise.setSets(sets);

        return exerciseRepository.save(exercise);
    }

    public List<TrainingExercise> getExercisesForFolder(String folderId) {
        return exerciseRepository.findByFolderId(folderId);
    }

    public TrainingExercise getExerciseById(String id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Exercise not found"
                        )
                );
    }
    public TrainingExercise updateExercise(
            String id,
            UpdateTrainingExerciseRequest request
    ) {
        TrainingExercise exercise = getExerciseById(id);

        exercise.setName(request.getName());
        exercise.setBodyRegion(request.getBodyRegion());
        exercise.setNotes(request.getNotes());
        exercise.setRestTimerSeconds(request.getRestTimerSeconds());

        if (request.getSets() != null) {
            exercise.setSets(
                    request.getSets().stream()
                            .map(s -> new SetEntry(
                                    s.order(),
                                    s.weight(),
                                    s.repetitions()
                            ))
                            .toList()
            );
        }

        return exerciseRepository.save(exercise);
    }

    public SetEntry updateSet(String exerciseId, int order, UpdateSetRequest request) {

        TrainingExercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        SetEntry set = exercise.getSets().stream()
                .filter(s -> s.getOrder() == order)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Set not found"));

        if (request.weight() != null) {
            set.setWeight(request.weight());
        }

        if (request.repetitions() != null) {
            set.setRepetitions(request.repetitions());
        }

        if (request.completed() != null) {
            set.setCompleted(request.completed());
        }

        exerciseRepository.save(exercise);
        return set;
    }

}