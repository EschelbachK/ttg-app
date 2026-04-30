package com.traintogain.backend.exercise;

import com.traintogain.backend.catalog.model.ExerciseCatalog;
import com.traintogain.backend.catalog.service.ExerciseCatalogService;
import com.traintogain.backend.exception.ForbiddenException;
import com.traintogain.backend.exception.NotFoundException;
import com.traintogain.backend.exercise.dto.CreateTrainingExerciseRequest;
import com.traintogain.backend.exercise.dto.SetEntryRequest;
import com.traintogain.backend.exercise.dto.UpdateTrainingExerciseRequest;
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
    private final ExerciseCatalogService catalogService;
    private final ExerciseValidationService validationService;

    public TrainingExerciseService(
            TrainingExerciseRepository trainingExerciseRepository,
            TrainingFolderRepository trainingFolderRepository,
            ExerciseCatalogService catalogService,
            ExerciseValidationService validationService
    ) {
        this.trainingExerciseRepository = trainingExerciseRepository;
        this.trainingFolderRepository = trainingFolderRepository;
        this.catalogService = catalogService;
        this.validationService = validationService;
    }

    public TrainingExercise addExercise(
            String userId,
            String planId,
            String folderId,
            CreateTrainingExerciseRequest request
    ) {
        TrainingFolder folder = getValidFolder(userId, planId, folderId);
        ExerciseCatalog catalog = getValidatedCatalog(request.exerciseId());
        List<SetEntry> sets = mapSets(request.sets());

        TrainingExercise exercise = new TrainingExercise();
        exercise.setUserId(userId);
        exercise.setFolderId(folder.getId());
        exercise.setExerciseId(catalog.getId());
        exercise.setSets(sets);
        exercise.prePersist();

        return trainingExerciseRepository.save(exercise);
    }

    public TrainingExercise updateExercise(
            String userId,
            String planId,
            String folderId,
            String exerciseId,
            UpdateTrainingExerciseRequest request
    ) {
        TrainingFolder folder = getValidFolder(userId, planId, folderId);

        TrainingExercise exercise = trainingExerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new NotFoundException("Übung nicht gefunden"));

        if (!exercise.getUserId().equals(userId) || !exercise.getFolderId().equals(folder.getId())) {
            throw new ForbiddenException("Kein Zugriff");
        }

        if (request.getExerciseId() != null) {
            ExerciseCatalog catalog = getValidatedCatalog(request.getExerciseId());
            exercise.setExerciseId(catalog.getId());
        }

        if (request.getSets() != null) {
            exercise.setSets(mapSets(request.getSets()));
        }

        exercise.preUpdate();
        return trainingExerciseRepository.save(exercise);
    }

    public Page<TrainingExercise> getExercisesByFolder(
            String userId,
            String planId,
            String folderId,
            Pageable pageable
    ) {
        TrainingFolder folder = getValidFolder(userId, planId, folderId);
        return trainingExerciseRepository.findByUserIdAndFolderId(userId, folder.getId(), pageable);
    }

    public void deleteExercise(String id, String userId) {
        TrainingExercise exercise = trainingExerciseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Übung nicht gefunden"));

        if (!exercise.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff");
        }

        trainingExerciseRepository.delete(exercise);
    }

    private TrainingFolder getValidFolder(String userId, String planId, String folderId) {
        TrainingFolder folder = trainingFolderRepository.findById(folderId)
                .orElseThrow(() -> new NotFoundException("Muskelgruppe nicht gefunden"));

        if (!folder.getUserId().equals(userId) || !folder.getTrainingPlanId().equals(planId)) {
            throw new ForbiddenException("Kein Zugriff auf diese Muskelgruppe");
        }

        return folder;
    }

    private ExerciseCatalog getValidatedCatalog(String exerciseId) {
        ExerciseCatalog catalog = catalogService.getById(exerciseId);
        validationService.validateExercise(catalog.getFamily(), catalog.getBasePattern());
        return catalog;
    }

    private List<SetEntry> mapSets(List<SetEntryRequest> sets) {
        if (sets == null) return List.of();
        return sets.stream()
                .map(s -> new SetEntry(s.weight(), s.repetitions()))
                .toList();
    }
}