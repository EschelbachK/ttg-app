package com.traintogain.backend.exercise;

import com.traintogain.backend.common.BodyRegion;
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
}