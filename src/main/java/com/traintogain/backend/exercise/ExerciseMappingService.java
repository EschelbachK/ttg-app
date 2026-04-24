package com.traintogain.backend.exercise;

import org.springframework.stereotype.Service;

@Service
public class ExerciseMappingService {

    public MappedExercise map(ExerciseInput input) {

        validate(input);

        return new MappedExercise(
                input.getId(),
                input.getName(),
                FamilyRegistry.valueOf(input.getFamily()),
                BasePatternRegistry.valueOf(input.getBasePattern()),
                input.getBodyRegion(),
                input.getEquipment(),
                input.getPrimaryMuscle(),
                input.getSecondaryMuscles(),
                input.getExerciseType(),
                input.getDifficulty()
        );
    }

    private void validate(ExerciseInput input) {
        FamilyRegistry.valueOf(input.getFamily());
        BasePatternRegistry.valueOf(input.getBasePattern());
    }
}