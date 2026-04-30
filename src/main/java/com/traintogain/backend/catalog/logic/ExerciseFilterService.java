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
                .filter(e -> match(bodyRegions, e.getBodyRegion()))
                .filter(e -> match(patterns, e.getMovementPattern()))
                .filter(e -> match(planes, e.getMovementPlane()))
                .filter(e -> match(mechanics, e.getMechanic()))
                .filter(e -> match(loadTypes, e.getLoadType()))
                .filter(e -> match(lateralities, e.getLaterality()))
                .filter(e -> matchMuscles(muscles, e))
                .filter(e -> matchEquipment(equipment, e))
                .toList();
    }

    private <T> boolean match(List<T> filter, T value) {
        return filter == null || filter.isEmpty() || filter.contains(value);
    }

    private boolean matchMuscles(List<Muscle> targets, ExerciseCatalog e) {

        if (targets == null || targets.isEmpty()) return true;

        Muscle primary = e.getPrimaryMuscle();
        List<Muscle> secondary = safe(e.getSecondaryMuscles());
        List<Muscle> stabilizers = safe(e.getStabilizers());

        for (Muscle m : targets) {
            if (m == primary) return true;
            if (secondary.contains(m)) return true;
            if (stabilizers.contains(m)) return true;
        }

        return false;
    }

    private boolean matchEquipment(List<EquipmentType> targets, ExerciseCatalog e) {

        if (targets == null || targets.isEmpty()) return true;

        List<EquipmentType> equipment = safe(e.getEquipment());

        for (EquipmentType eq : targets) {
            if (equipment.contains(eq)) return true;
        }

        return false;
    }

    private <T> List<T> safe(List<T> list) {
        return list == null ? List.of() : list;
    }
}