package com.traintogain.backend.exercise;

import com.traintogain.backend.catalog.model.EquipmentType;
import com.traintogain.backend.catalog.model.ExerciseCatalog;
import com.traintogain.backend.catalog.model.ExerciseMedia;
import com.traintogain.backend.catalog.model.ExerciseTag;
import com.traintogain.backend.catalog.model.Muscle;
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

        applyCatalogSnapshot(exercise, catalog);

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
            applyCatalogSnapshot(exercise, catalog);
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

    private void applyCatalogSnapshot(TrainingExercise exercise, ExerciseCatalog catalog) {
        exercise.setName(catalog.getName());
        exercise.setBodyRegion(catalog.getBodyRegion() != null ? catalog.getBodyRegion().name() : null);
        exercise.setFamily(catalog.getFamily() != null ? catalog.getFamily().name() : null);
        exercise.setMovementPattern(catalog.getMovementPattern() != null ? catalog.getMovementPattern().name() : null);
        exercise.setEquipment(mapEquipment(catalog.getEquipment()));
        exercise.setPrimaryMuscle(catalog.getPrimaryMuscle() != null ? catalog.getPrimaryMuscle().name() : null);
        exercise.setSecondaryMuscles(mapMuscles(catalog.getSecondaryMuscles()));
        exercise.setStabilizers(mapMuscles(catalog.getStabilizers()));
        exercise.setExerciseType(catalog.getExerciseType() != null ? catalog.getExerciseType().name() : null);
        exercise.setDifficulty(catalog.getDifficulty() != null ? catalog.getDifficulty().name() : null);
        exercise.setTags(mapTags(catalog.getTags()));
        exercise.setInstructions(splitInstructions(catalog.getInstructions()));
        exercise.setTips(catalog.getTips() == null ? List.of() : catalog.getTips());
        exercise.setCommonMistakes(catalog.getCommonMistakes() == null ? List.of() : catalog.getCommonMistakes());
        applyMediaSnapshot(exercise, catalog.getMedia());
    }

    private void applyMediaSnapshot(TrainingExercise exercise, ExerciseMedia media) {
        TrainingExercise.Media snapshot = new TrainingExercise.Media();

        if (media != null) {
            snapshot.setImage(media.getImageFile() == null ? "" : media.getImageFile());
            snapshot.setThumbnail(media.getThumbnailFile() == null ? "" : media.getThumbnailFile());
            snapshot.setAnimation(media.getAnimationFile() == null ? "" : media.getAnimationFile());
        }

        exercise.setMedia(snapshot);
    }

    private String mapEquipment(List<EquipmentType> equipment) {
        if (equipment == null || equipment.isEmpty()) return "";
        return equipment.stream()
                .map(Enum::name)
                .findFirst()
                .orElse("");
    }

    private List<String> mapMuscles(List<Muscle> muscles) {
        if (muscles == null) return List.of();
        return muscles.stream()
                .map(Enum::name)
                .toList();
    }

    private List<String> mapTags(List<ExerciseTag> tags) {
        if (tags == null) return List.of();
        return tags.stream()
                .map(Enum::name)
                .toList();
    }

    private List<String> splitInstructions(String raw) {
        if (raw == null || raw.isBlank()) return List.of();

        return List.of(raw.split("\\.")).stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private List<SetEntry> mapSets(List<SetEntryRequest> sets) {
        if (sets == null) return List.of();
        return sets.stream()
                .map(s -> new SetEntry(s.weight(), s.repetitions()))
                .toList();
    }
}