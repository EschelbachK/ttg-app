package com.traintogain.backend.catalog.logic;

import com.traintogain.backend.catalog.model.ExerciseCatalog;
import com.traintogain.backend.catalog.model.Muscle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MuscleCoverageService {

    private static final int PRIMARY_WEIGHT = 3;
    private static final int SECONDARY_WEIGHT = 2;
    private static final int STABILIZER_WEIGHT = 1;

    public Map<Muscle, Integer> analyze(List<ExerciseCatalog> exercises) {

        Map<Muscle, Integer> map = new HashMap<>();

        if (exercises == null || exercises.isEmpty()) {
            return map;
        }

        for (ExerciseCatalog e : exercises) {

            increment(map, e.getPrimaryMuscle(), PRIMARY_WEIGHT);

            for (Muscle m : safe(e.getSecondaryMuscles())) {
                increment(map, m, SECONDARY_WEIGHT);
            }

            for (Muscle m : safe(e.getStabilizers())) {
                increment(map, m, STABILIZER_WEIGHT);
            }
        }

        return map;
    }

    private void increment(Map<Muscle, Integer> map, Muscle muscle, int value) {
        if (muscle == null) return;
        map.put(muscle, map.getOrDefault(muscle, 0) + value);
    }

    private List<Muscle> safe(List<Muscle> list) {
        return list == null ? List.of() : list;
    }
}