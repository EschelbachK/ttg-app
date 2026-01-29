package com.traintogain.backend.exercise;

import com.traintogain.backend.common.BodyRegion;
import org.springframework.stereotype.Service;

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
}