package com.traintogain.backend.catalog.seed;

import com.traintogain.backend.catalog.model.*;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDataSanitizer {

    public static List<RiskType> sanitizeRisks(List<RiskType> risks) {
        if (risks == null) return List.of();

        List<RiskType> clean = new ArrayList<>();

        for (RiskType r : risks) {
            if (r != null) {
                clean.add(r);
            }
        }

        return clean;
    }

    public static List<Muscle> sanitizeMuscles(List<Muscle> muscles) {
        if (muscles == null) return List.of();

        List<Muscle> clean = new ArrayList<>();

        for (Muscle m : muscles) {
            if (m != null) {
                clean.add(m);
            }
        }

        return clean;
    }

    public static List<EquipmentType> sanitizeEquipment(List<EquipmentType> equipment) {
        if (equipment == null) return List.of();

        List<EquipmentType> clean = new ArrayList<>();

        for (EquipmentType e : equipment) {
            if (e != null) {
                clean.add(e);
            }
        }

        return clean;
    }

    public static List<ExerciseTag> sanitizeTags(List<ExerciseTag> tags) {
        if (tags == null) return List.of();

        List<ExerciseTag> clean = new ArrayList<>();

        for (ExerciseTag t : tags) {
            if (t != null) {
                clean.add(t);
            }
        }

        return clean;
    }
}