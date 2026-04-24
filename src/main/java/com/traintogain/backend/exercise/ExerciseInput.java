package com.traintogain.backend.exercise;

import java.util.List;

public class ExerciseInput {

    private String id;
    private String name;
    private String family;
    private String basePattern;
    private String bodyRegion;
    private String equipment;
    private String primaryMuscle;
    private List<String> secondaryMuscles;
    private String exerciseType;
    private String difficulty;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getFamily() { return family; }
    public String getBasePattern() { return basePattern; }
    public String getBodyRegion() { return bodyRegion; }
    public String getEquipment() { return equipment; }
    public String getPrimaryMuscle() { return primaryMuscle; }
    public List<String> getSecondaryMuscles() { return secondaryMuscles; }
    public String getExerciseType() { return exerciseType; }
    public String getDifficulty() { return difficulty; }
}