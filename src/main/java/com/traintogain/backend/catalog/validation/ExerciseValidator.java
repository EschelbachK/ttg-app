package com.traintogain.backend.catalog.validation;

import com.traintogain.backend.catalog.model.*;
import com.traintogain.backend.exercise.BasePatternRegistry;
import com.traintogain.backend.exercise.ExerciseFamily;

import java.util.*;

public final class ExerciseValidator {

    private ExerciseValidator() {}

    public static List<String> validate(ExerciseCatalog e) {

        if (e == null) {
            return List.of("exercise null");
        }

        // Sicherstellen, dass Default-Werte gesetzt sind
        e.ensureSafeDefaults();

        List<String> errors = new ArrayList<>();

        if (isBlank(e.getId())) errors.add("id missing");
        if (isBlank(e.getName())) errors.add("name missing");

        if (e.getBodyRegion() == null) errors.add("bodyRegion missing");
        if (e.getFamily() == null) errors.add("family missing");
        if (e.getMovementPattern() == null) errors.add("movementPattern missing");
        if (e.getBasePattern() == null) errors.add("basePattern missing");

        if (e.getPrimaryMuscle() == null) errors.add("primaryMuscle missing");
        if (e.getEquipment() == null) errors.add("equipment missing");

        if (e.getExerciseType() == null) errors.add("exerciseType missing");
        if (e.getDifficulty() == null) errors.add("difficulty missing");

        if (e.getMedia() == null) errors.add("media missing");
        if (e.getExecution() == null) errors.add("execution missing");

        // Prüfe Execution + speedType
        validateExecution(e, errors);

        validateTags(e, errors);
        validateMuscles(e, errors);
        validateProgression(e, errors);
        validateDomainConsistency(e, errors);
        validateContent(e, errors);

        return errors;
    }

    private static void validateExecution(ExerciseCatalog e, List<String> errors) {

        // Execution prüfen
        Execution ex = e.getExecution();
        if (ex != null) {
            if (!ex.isValidTempo()) {
                errors.add("invalid tempo format");
            }
            if (ex.getRangeOfMotion() == null) {
                errors.add("rangeOfMotion missing");
            }
        }

        // speedType auf oberster Ebene prüfen
        if (e.getSpeedType() == null) {
            errors.add("speedType missing");
        }
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

    private static void validateProgression(ExerciseCatalog e, List<String> errors) {

        Progression p = e.getProgression();
        if (p == null) return;

        List<String> regressions = safe(p.getRegressions());
        List<String> progressions = safe(p.getProgressions());

        if (hasDuplicates(regressions)) {
            errors.add("duplicate regressions");
        }

        if (hasDuplicates(progressions)) {
            errors.add("duplicate progressions");
        }

        if (regressions.contains(e.getId())) {
            errors.add("self reference in regressions");
        }

        if (progressions.contains(e.getId())) {
            errors.add("self reference in progressions");
        }
    }

    private static void validateDomainConsistency(ExerciseCatalog e, List<String> errors) {

        if (e.getMovementPattern() == MovementPattern.PUSH &&
                e.getFamily() == ExerciseFamily.PULL) {
            errors.add("invalid mismatch: PUSH pattern with PULL family");
        }

        if (e.getMovementPattern() == MovementPattern.SQUAT &&
                e.getBasePattern() != BasePatternRegistry.SQUAT_PATTERN) {
            errors.add("squat pattern mismatch");
        }

        if (e.getMovementPattern() == MovementPattern.HINGE &&
                e.getBasePattern() != BasePatternRegistry.HIP_HINGE) {
            errors.add("hinge pattern mismatch");
        }
    }

    private static void validateContent(ExerciseCatalog e, List<String> errors) {

        if (safe(e.getInstructions()).isEmpty()) {
            errors.add("instructions missing");
        }

        if (safe(e.getTips()).isEmpty()) {
            errors.add("tips missing");
        }

        if (safe(e.getCommonMistakes()).isEmpty()) {
            errors.add("commonMistakes missing");
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