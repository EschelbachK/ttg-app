package com.traintogain.backend.exercise;

import com.traintogain.backend.exception.ForbiddenException;
import com.traintogain.backend.exception.NotFoundException;
import com.traintogain.backend.exercise.dto.CreateTrainingExerciseRequest;
import com.traintogain.backend.folder.TrainingFolder;
import com.traintogain.backend.folder.TrainingFolderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingExerciseService {

    private final TrainingExerciseRepository trainingExerciseRepository;
    private final TrainingFolderRepository trainingFolderRepository;

    public TrainingExerciseService(
            TrainingExerciseRepository trainingExerciseRepository,
            TrainingFolderRepository trainingFolderRepository
    ) {
        this.trainingExerciseRepository = trainingExerciseRepository;
        this.trainingFolderRepository = trainingFolderRepository;
    }

    public TrainingExercise addExercise(String userId, String planId, CreateTrainingExerciseRequest request) {

        TrainingFolder folder = trainingFolderRepository.findById(request.folderId())
                .orElseThrow(() -> new NotFoundException("Ordner nicht gefunden"));

        if (!folder.getUserId().equals(userId) || !folder.getTrainingPlanId().equals(planId)) {
            throw new ForbiddenException("Kein Zugriff auf diesen Ordner");
        }

        if (request.sets() == null || request.sets().isEmpty()) {
            throw new IllegalArgumentException("Die Übung muss mindestens einen Satz enthalten");
        }

        TrainingExercise exercise = new TrainingExercise();
        exercise.setUserId(userId);
        exercise.setFolderId(request.folderId());
        exercise.setName(request.name());

        List<SetEntry> sets = request.sets().stream()
                .map(s -> new SetEntry(s.weight(), s.repetitions()))
                .toList();

        exercise.setSets(sets);

        return trainingExerciseRepository.save(exercise);
    }

    public List<TrainingExercise> getExercisesByFolder(String userId, String planId, String folderId) {

        TrainingFolder folder = trainingFolderRepository.findById(folderId)
                .orElseThrow(() -> new NotFoundException("Ordner nicht gefunden"));

        if (!folder.getUserId().equals(userId) || !folder.getTrainingPlanId().equals(planId)) {
            throw new ForbiddenException("Kein Zugriff auf diesen Ordner");
        }

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