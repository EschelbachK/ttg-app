package com.traintogain.backend.catalog.logic;

import com.traintogain.backend.catalog.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseFilterService {

    public List<ExerciseCatalog> filter(
            List<ExerciseCatalog> exercises,
            List<BodyRegion> bodyRegions,
            List<Muscle> muscles,
            List<MovementPattern> patterns,
            List<EquipmentType> equipment,
            List<MovementPlane> planes,
            List<MovementMechanic> mechanics,
            List<LoadType> loadTypes,
            List<Laterality> lateralities
    ) {
        return exercises.stream()
                .filter(e -> bodyRegions.isEmpty() || bodyRegions.contains(e.getBodyRegion()))
                .filter(e -> muscles.isEmpty() || matchesMuscles(e, muscles))
                .filter(e -> patterns.isEmpty() || patterns.contains(e.getMovementPattern()))
                .filter(e -> equipment.isEmpty() || matchesEquipment(e, equipment))
                .filter(e -> planes.isEmpty() || planes.contains(e.getMovementPlane()))
                .filter(e -> mechanics.isEmpty() || mechanics.contains(e.getMechanic()))
                .filter(e -> loadTypes.isEmpty() || loadTypes.contains(e.getLoadType()))
                .filter(e -> lateralities.isEmpty() || lateralities.contains(e.getLaterality()))
                .toList();
    }

    private boolean matchesMuscles(ExerciseCatalog e, List<Muscle> targets) {
        if (targets.stream().anyMatch(m -> m == e.getPrimaryMuscle())) return true;

        if (e.getSecondaryMuscles() != null &&
                targets.stream().anyMatch(e.getSecondaryMuscles()::contains)) return true;

        return e.getStabilizers() != null &&
                targets.stream().anyMatch(e.getStabilizers()::contains);
    }

    private boolean matchesEquipment(ExerciseCatalog e, List<EquipmentType> target) {
        if (e.getEquipment() == null) return false;
        return e.getEquipment().stream().anyMatch(target::contains);
    }
}