package com.traintogain.backend.exercise;

import com.traintogain.backend.catalog.model.EquipmentType;
import com.traintogain.backend.catalog.model.ExerciseCatalog;
import com.traintogain.backend.catalog.repository.ExerciseCatalogRepository;
import com.traintogain.backend.exercise.dto.RankedExerciseResponse;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class SmartAlternativeRankingEngine {

    private final ExerciseCatalogRepository catalogRepository;
    private Map<String, ExerciseCatalog> cache = new ConcurrentHashMap<>();

    public SmartAlternativeRankingEngine(ExerciseCatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public List<RankedExerciseResponse> rank(String exerciseId, List<TrainingExercise> candidates) {
        Map<String, ExerciseCatalog> catalogMap = getCatalogMap();

        ExerciseCatalog base = catalogMap.get(exerciseId);
        if (base == null || candidates == null) return List.of();

        return candidates.stream()
                .filter(Objects::nonNull)
                .filter(ex -> ex.getExerciseId() != null)
                .filter(ex -> !ex.getExerciseId().equals(exerciseId))
                .map(ex -> scoreCandidate(ex, base, catalogMap))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(RankedExerciseResponse::getScore).reversed())
                .toList();
    }

    private RankedExerciseResponse scoreCandidate(
            TrainingExercise ex,
            ExerciseCatalog base,
            Map<String, ExerciseCatalog> catalogMap
    ) {
        ExerciseCatalog other = catalogMap.get(ex.getExerciseId());
        if (other == null) return null;

        int score = 0;

        if (sameFamily(base, other)) {
            score += Score.FAMILY;
        }

        if (sameBasePattern(base, other)) {
            score += Score.BASE_PATTERN;
        }

        if (samePrimaryMuscle(base, other)) {
            score += Score.MUSCLE;
        }

        score += equipmentScore(ex, other);

        return RankedExerciseResponse.from(other, score);
    }

    private boolean sameFamily(ExerciseCatalog base, ExerciseCatalog other) {
        return base.getFamily() != null && base.getFamily() == other.getFamily();
    }

    private boolean sameBasePattern(ExerciseCatalog base, ExerciseCatalog other) {
        return base.getBasePattern() != null && base.getBasePattern() == other.getBasePattern();
    }

    private boolean samePrimaryMuscle(ExerciseCatalog base, ExerciseCatalog other) {
        return base.getPrimaryMuscle() != null && base.getPrimaryMuscle() == other.getPrimaryMuscle();
    }

    private int equipmentScore(TrainingExercise ex, ExerciseCatalog other) {
        if (ex == null || other.getEquipment() == null || ex.getEquipment() == null) return 0;

        String userEquipment = ex.getEquipment().trim().toUpperCase();

        for (EquipmentType eq : other.getEquipment()) {
            if (eq.name().equals(userEquipment)) {
                return Score.EQUIPMENT_MATCH;
            }
        }

        return 0;
    }

    private Map<String, ExerciseCatalog> getCatalogMap() {
        if (cache == null || cache.isEmpty()) {
            cache = catalogRepository.findAll().stream()
                    .filter(Objects::nonNull)
                    .filter(e -> e.getId() != null)
                    .collect(Collectors.toMap(
                            ExerciseCatalog::getId,
                            e -> e,
                            (a, b) -> a,
                            ConcurrentHashMap::new
                    ));
        }
        return cache;
    }

    private static class Score {
        private static final int FAMILY = 40;
        private static final int BASE_PATTERN = 25;
        private static final int MUSCLE = 15;
        private static final int EQUIPMENT_MATCH = 10;
    }
}