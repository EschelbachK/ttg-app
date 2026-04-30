package com.traintogain.backend.exercise;

import com.traintogain.backend.catalog.model.ExerciseCatalog;
import com.traintogain.backend.catalog.repository.ExerciseCatalogRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExerciseAlternativesService {

    private final TrainingExerciseRepository repository;
    private final ExerciseCatalogRepository catalogRepository;
    private Map<String, ExerciseCatalog> cache;

    public ExerciseAlternativesService(
            TrainingExerciseRepository repository,
            ExerciseCatalogRepository catalogRepository
    ) {
        this.repository = repository;
        this.catalogRepository = catalogRepository;
    }

    public List<TrainingExercise> getAlternatives(String exerciseId) {
        return findAlternatives(exerciseId, true, true);
    }

    public List<TrainingExercise> getFamilyAlternatives(String exerciseId) {
        return findAlternatives(exerciseId, true, false);
    }

    public List<TrainingExercise> getBasePatternAlternatives(String exerciseId) {
        return findAlternatives(exerciseId, false, true);
    }

    private List<TrainingExercise> findAlternatives(
            String exerciseId,
            boolean checkFamily,
            boolean checkPattern
    ) {
        Map<String, ExerciseCatalog> catalogMap = getCatalogMap();
        ExerciseCatalog base = catalogMap.get(exerciseId);
        if (base == null) return List.of();

        return repository.findAll().stream()
                .filter(ex -> isValidCandidate(ex, exerciseId, catalogMap))
                .filter(ex -> matchesCriteria(ex, base, catalogMap, checkFamily, checkPattern))
                .toList();
    }

    private boolean isValidCandidate(
            TrainingExercise ex,
            String exerciseId,
            Map<String, ExerciseCatalog> catalogMap
    ) {
        return ex.getExerciseId() != null
                && !ex.getExerciseId().equals(exerciseId)
                && catalogMap.containsKey(ex.getExerciseId());
    }

    private boolean matchesCriteria(
            TrainingExercise ex,
            ExerciseCatalog base,
            Map<String, ExerciseCatalog> catalogMap,
            boolean checkFamily,
            boolean checkPattern
    ) {
        ExerciseCatalog other = catalogMap.get(ex.getExerciseId());
        if (other == null) return false;

        boolean sameFamily =
                checkFamily
                        && base.getFamily() != null
                        && base.getFamily() == other.getFamily();

        boolean sameBasePattern =
                checkPattern
                        && base.getBasePattern() != null
                        && base.getBasePattern() == other.getBasePattern();

        return sameFamily || sameBasePattern;
    }

    private Map<String, ExerciseCatalog> getCatalogMap() {
        if (cache == null || cache.isEmpty()) {
            cache = catalogRepository.findAll().stream()
                    .filter(Objects::nonNull)
                    .filter(e -> e.getId() != null)
                    .collect(Collectors.toMap(
                            ExerciseCatalog::getId,
                            Function.identity(),
                            (a, b) -> a
                    ));
        }
        return cache;
    }
}