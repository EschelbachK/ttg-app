package com.traintogain.backend.catalog.validation;

import com.traintogain.backend.catalog.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ExerciseValidator {

    private ExerciseValidator() {}

    public static List<String> validate(ExerciseCatalog e) {

        List<String> errors = new ArrayList<>();

        if (e == null) {
            errors.add("exercise null");
            return errors;
        }

        if (isBlank(e.getId())) errors.add("id missing");
        if (isBlank(e.getName())) errors.add("name missing");

        if (e.getBodyRegion() == null) errors.add("bodyRegion missing");
        if (e.getFamily() == null) errors.add("family missing");
        if (e.getMovementPattern() == null) errors.add("movementPattern missing");
        if (e.getBasePattern() == null) errors.add("basePattern missing");

        if (e.getPrimaryMuscle() == null) errors.add("primaryMuscle missing");

        if (e.getExerciseType() == null) errors.add("exerciseType missing");
        if (e.getDifficulty() == null) errors.add("difficulty missing");

        if (e.getExecution() == null) errors.add("execution missing");
        if (e.getMedia() == null) errors.add("media missing");

        validateExecution(e, errors);
        validateMuscles(e, errors);
        validateTags(e, errors);

        return errors;
    }

    private static void validateExecution(ExerciseCatalog e, List<String> errors) {

        Execution ex = e.getExecution();
        if (ex == null) return;

        if (isBlank(ex.getTempo())) errors.add("tempo missing");
        if (ex.getRangeOfMotion() == null) errors.add("rangeOfMotion missing");
    }

    private static void validateTags(ExerciseCatalog e, List<String> errors) {

        List<ExerciseTag> tags = safe(e.getTags());

        if (tags.isEmpty()) {
            errors.add("tags missing");
        }

        if (hasDuplicates(tags)) {
            errors.add("duplicate tags");
        }
    }

    private static void validateMuscles(ExerciseCatalog e, List<String> errors) {

        Muscle primary = e.getPrimaryMuscle();
        if (primary == null) return;

        List<Muscle> secondary = safe(e.getSecondaryMuscles());
        List<Muscle> stabilizers = safe(e.getStabilizers());

        if (secondary.contains(primary)) {
            errors.add("primary muscle duplicated in secondary");
        }

        if (stabilizers.contains(primary)) {
            errors.add("primary muscle duplicated in stabilizers");
        }

        if (hasDuplicates(secondary)) {
            errors.add("duplicate secondary muscles");
        }

        if (hasDuplicates(stabilizers)) {
            errors.add("duplicate stabilizers");
        }
    }

    private static <T> boolean hasDuplicates(List<T> list) {
        return new HashSet<>(list).size() != list.size();
    }

    private static <T> List<T> safe(List<T> list) {
        return list == null ? List.of() : list;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}