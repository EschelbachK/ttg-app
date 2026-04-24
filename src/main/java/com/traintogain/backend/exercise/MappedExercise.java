package com.traintogain.backend.exercise;

import java.util.List;

public class MappedExercise {

    private final String id;
    private final String name;
    private final FamilyRegistry family;
    private final BasePatternRegistry basePattern;
    private final String bodyRegion;
    private final String equipment;
    private final String primaryMuscle;
    private final List<String> secondaryMuscles;
    private final String exerciseType;
    private final String difficulty;

    public MappedExercise(
            String id,
            String name,
            FamilyRegistry family,
            BasePatternRegistry basePattern,
            String bodyRegion,
            String equipment,
            String primaryMuscle,
            List<String> secondaryMuscles,
            String exerciseType,
            String difficulty
    ) {
        this.id = id;
        this.name = name;
        this.family = family;
        this.basePattern = basePattern;
        this.bodyRegion = bodyRegion;
        this.equipment = equipment;
        this.primaryMuscle = primaryMuscle;
        this.secondaryMuscles = secondaryMuscles;
        this.exerciseType = exerciseType;
        this.difficulty = difficulty;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public FamilyRegistry getFamily() { return family; }
    public BasePatternRegistry getBasePattern() { return basePattern; }
    public String getBodyRegion() { return bodyRegion; }
    public String getEquipment() { return equipment; }
    public String getPrimaryMuscle() { return primaryMuscle; }
    public List<String> getSecondaryMuscles() { return secondaryMuscles; }
    public String getExerciseType() { return exerciseType; }
    public String getDifficulty() { return difficulty; }
}