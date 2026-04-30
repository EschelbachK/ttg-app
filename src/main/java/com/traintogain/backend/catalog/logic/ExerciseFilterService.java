package com.traintogain.backend.catalog.logic;

import com.traintogain.backend.catalog.model.*;

import java.util.List;

public class ExerciseFilterEngine {

    private ExerciseFilterEngine() {}

    public static List<ExerciseCatalog> filter(
            List<ExerciseCatalog> exercises,
            BodyRegion bodyRegion,
            Muscle muscle,
            MovementPattern pattern,
            EquipmentType equipment
    ) {
        return exercises.stream()
                .filter(e -> bodyRegion == null || bodyRegion == e.getBodyRegion())
                .filter(e -> muscle == null || matchesMuscle(e, muscle))
                .filter(e -> pattern == null || pattern == e.getMovementPattern())
                .filter(e -> equipment == null || equipment == e.getEquipment())
                .toList();
    }

    private static boolean matchesMuscle(ExerciseCatalog e, Muscle target) {
        if (e.getPrimaryMuscle() == target) return true;
        if (e.getSecondaryMuscles() != null && e.getSecondaryMuscles().contains(target)) return true;
        return e.getStabilizers() != null && e.getStabilizers().contains(target);
    }
}