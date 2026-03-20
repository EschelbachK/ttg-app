package com.traintogain.backend.exercise;

import com.traintogain.backend.common.exception.NotFoundException;
import com.traintogain.backend.training.dto.CreateTrainingExerciseRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingExerciseService {

    private final TrainingExerciseRepository trainingExerciseRepository;

    public TrainingExerciseService(TrainingExerciseRepository trainingExerciseRepository) {
        this.trainingExerciseRepository = trainingExerciseRepository;
    }

    public TrainingExercise addExercise(String userId, CreateTrainingExerciseRequest request) {

        if (request.sets() == null || request.sets().isEmpty()) {
            throw new IllegalArgumentException("Exercise must contain at least one set");
        }

        TrainingExercise exercise = new TrainingExercise();
        exercise.setUserId(userId);
        exercise.setFolderId(request.folderId());
        exercise.setName(request.name());

        List<SetEntry> sets = request.sets().stream()
                .map(s -> new SetEntry(
                        s.weight(),
                        s.reps()
                ))
                .toList();

        exercise.setSets(sets);

        return trainingExerciseRepository.save(exercise);
    }

    public List<TrainingExercise> getExercisesByFolder(String userId, String folderId) {
        return trainingExerciseRepository.findByUserIdAndFolderId(userId, folderId);
    }

    public void deleteExercise(String id) {
        TrainingExercise exercise = trainingExerciseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("TrainingExercise not found"));

        trainingExerciseRepository.delete(exercise);
    }
}