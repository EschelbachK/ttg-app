package com.traintogain.backend.exercise;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExerciseAlternativesService {

    private final TrainingExerciseRepository repository;
    private final ExerciseReferenceResolver resolver;

    public ExerciseAlternativesService(
            TrainingExerciseRepository repository,
            ExerciseReferenceResolver resolver
    ) {
        this.repository = repository;
        this.resolver = resolver;
    }

    public List<TrainingExercise> getAlternatives(String exerciseId) {

        ExerciseReference ref = resolver.resolve(exerciseId);

        List<TrainingExercise> all = repository.findAll();

        List<TrainingExercise> result = new ArrayList<>();

        for (TrainingExercise ex : all) {

            if (ex.getExerciseId() == null) continue;

            ExerciseReference other = resolver.resolve(ex.getExerciseId());

            boolean sameFamily = other.getFamily() == ref.getFamily();
            boolean sameBasePattern = other.getBasePattern() == ref.getBasePattern();

            if (sameFamily || sameBasePattern) {
                if (!ex.getExerciseId().equals(exerciseId)) {
                    result.add(ex);
                }
            }
        }

        return result;
    }

    public List<TrainingExercise> getFamilyAlternatives(String exerciseId) {

        ExerciseReference ref = resolver.resolve(exerciseId);

        List<TrainingExercise> all = repository.findAll();

        List<TrainingExercise> result = new ArrayList<>();

        for (TrainingExercise ex : all) {

            if (ex.getExerciseId() == null) continue;

            ExerciseReference other = resolver.resolve(ex.getExerciseId());

            if (other.getFamily() == ref.getFamily()
                    && !ex.getExerciseId().equals(exerciseId)) {
                result.add(ex);
            }
        }

        return result;
    }

    public List<TrainingExercise> getBasePatternAlternatives(String exerciseId) {

        ExerciseReference ref = resolver.resolve(exerciseId);

        List<TrainingExercise> all = repository.findAll();

        List<TrainingExercise> result = new ArrayList<>();

        for (TrainingExercise ex : all) {

            if (ex.getExerciseId() == null) continue;

            ExerciseReference other = resolver.resolve(ex.getExerciseId());

            if (other.getBasePattern() == ref.getBasePattern()
                    && !ex.getExerciseId().equals(exerciseId)) {
                result.add(ex);
            }
        }

        return result;
    }
}