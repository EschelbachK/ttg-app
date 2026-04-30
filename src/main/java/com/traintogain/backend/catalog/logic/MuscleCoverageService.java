package com.traintogain.backend.catalog.logic;

import com.traintogain.backend.catalog.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MuscleCoverageAnalyzer {

    public static Map<Muscle, Integer> analyze(List<ExerciseCatalog> exercises) {
        Map<Muscle, Integer> map = new HashMap<>();
        for (ExerciseCatalog e : exercises) {
            increment(map, e.getPrimaryMuscle(), 3);
            for (Muscle m : safe(e.getSecondaryMuscles())) increment(map, m, 2);
            for (Muscle m : safe(e.getStabilizers())) increment(map, m, 1);
        }
        return map;
    }

    private static void increment(Map<Muscle, Integer> map, Muscle m, int value) {
        if (m != null) map.put(m, map.getOrDefault(m, 0) + value);
    }

    private static List<Muscle> safe(List<Muscle> list) {
        return list == null ? List.of() : list;
    }
}