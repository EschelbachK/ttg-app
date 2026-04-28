package com.traintogain.backend.exercise;

import com.traintogain.backend.catalog.model.ExerciseCatalog;
import com.traintogain.backend.catalog.model.Muscle;
import com.traintogain.backend.catalog.repository.ExerciseCatalogRepository;
import com.traintogain.backend.exercise.dto.RankedExerciseResponse;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SmartAlternativeRankingEngine {

    private final ExerciseCatalogRepository catalogRepository;
    private Map<String, ExerciseCatalog> cache = new HashMap<>();

    public SmartAlternativeRankingEngine(ExerciseCatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public List<RankedExerciseResponse> rank(String exerciseId, List<TrainingExercise> candidates) {
        Map<String, ExerciseCatalog> catalogMap = getCatalogMap();
        ExerciseCatalog base = catalogMap.get(exerciseId);
        if (base == null) return List.of();

        return candidates.stream()
                .filter(ex -> ex.getExerciseId() != null && !ex.getExerciseId().equals(exerciseId))
                .map(ex -> {
                    ExerciseCatalog other = catalogMap.get(ex.getExerciseId());
                    if (other == null) return null;

                    int score = 0;
                    if (other.getFamily() == base.getFamily()) score += 40;
                    if (other.getBasePattern() == base.getBasePattern()) score += 25;
                    if (isSameMuscle(other.getPrimaryMuscle(), base.getPrimaryMuscle())) score += 15;
                    score += equipmentScore(ex);

                    return RankedExerciseResponse.from(other, score);
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(RankedExerciseResponse::getScore).reversed())
                .toList();
    }

    private Map<String, ExerciseCatalog> getCatalogMap() {
        if (cache.isEmpty()) {
            cache = catalogRepository.findAll().stream()
                    .collect(Collectors.toMap(ExerciseCatalog::getId, e -> e));
        }
        return cache;
    }

    private int equipmentScore(TrainingExercise ex) { return 0; }

    private boolean isSameMuscle(Muscle a, Muscle b) { return a != null && a == b; }
}