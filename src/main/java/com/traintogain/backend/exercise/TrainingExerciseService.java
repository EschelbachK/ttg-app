package com.traintogain.backend.exercise;

import com.traintogain.backend.exception.ForbiddenException;
import com.traintogain.backend.exception.NotFoundException;
import com.traintogain.backend.exercise.dto.CreateTrainingExerciseRequest;
import com.traintogain.backend.folder.TrainingFolder;
import com.traintogain.backend.folder.TrainingFolderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public TrainingExercise addExercise(
            String userId,
            String planId,
            String folderId,
            CreateTrainingExerciseRequest request
    ) {

        TrainingFolder folder = trainingFolderRepository.findById(folderId)
                .orElseThrow(() -> new NotFoundException("Muskelgruppe nicht gefunden"));

        if (!folder.getUserId().equals(userId) ||
                !folder.getTrainingPlanId().equals(planId)) {
            throw new ForbiddenException("Kein Zugriff auf diese Muskelgruppe");
        }

        List<SetEntry> sets = request.sets() != null
                ? request.sets().stream()
                .map(s -> new SetEntry(s.weight(), s.repetitions()))
                .toList()
                : List.of();

        TrainingExercise exercise = new TrainingExercise();
        exercise.setUserId(userId);
        exercise.setFolderId(folderId);
        exercise.setName(request.name());
        exercise.setSets(sets);

        return trainingExerciseRepository.save(exercise);
    }

    public Page<TrainingExercise> getExercisesByFolder(
            String userId,
            String planId,
            String folderId,
            Pageable pageable
    ) {

        TrainingFolder folder = trainingFolderRepository.findById(folderId)
                .orElseThrow(() -> new NotFoundException("Muskelgruppe nicht gefunden"));

        if (!folder.getUserId().equals(userId) ||
                !folder.getTrainingPlanId().equals(planId)) {
            throw new ForbiddenException("Kein Zugriff");
        }

        return trainingExerciseRepository.findByUserIdAndFolderId(
                userId,
                folderId,
                pageable
        );
    }

    public void deleteExercise(String id, String userId) {

        TrainingExercise exercise = trainingExerciseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Übung nicht gefunden"));

        if (!exercise.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff");
        }

        trainingExerciseRepository.delete(exercise);
    }
}