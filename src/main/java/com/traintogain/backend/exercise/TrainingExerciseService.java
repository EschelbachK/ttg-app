package com.traintogain.backend.exercise;

import com.traintogain.backend.exception.ForbiddenException;
import com.traintogain.backend.exception.NotFoundException;
import com.traintogain.backend.exercise.dto.CreateTrainingExerciseRequest;
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
            throw new IllegalArgumentException("Die Übung muss mindestens einen Satz enthalten");
        }

        TrainingExercise exercise = new TrainingExercise();
        exercise.setUserId(userId);
        exercise.setFolderId(request.folderId());
        exercise.setName(request.name());

        List<SetEntry> sets = request.sets().stream()
                .map(s -> new SetEntry(
                        s.weight(),
                        s.repetitions()
                ))
                .toList();

        exercise.setSets(sets);

        return trainingExerciseRepository.save(exercise);
    }

    public List<TrainingExercise> getExercisesByFolder(String userId, String folderId) {
        return trainingExerciseRepository.findByUserIdAndFolderId(userId, folderId);
    }

    public void deleteExercise(String id, String userId) {
        TrainingExercise exercise = trainingExerciseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Übung wurde nicht gefunden"));

        if (!exercise.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff auf diese Übung");
        }

        trainingExerciseRepository.delete(exercise);
    }
}