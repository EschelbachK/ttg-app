package com.traintogain.backend.exercise;

public class ExerciseReference {

    private ExerciseFamily family;
    private BasePatternRegistry basePattern;
    private String primaryMuscle;

    public ExerciseFamily getFamily() {
        return family;
    }

    public void setFamily(ExerciseFamily family) {
        this.family = family;
    }

    public BasePatternRegistry getBasePattern() {
        return basePattern;
    }

    public void setBasePattern(BasePatternRegistry basePattern) {
        this.basePattern = basePattern;
    }

    public String getPrimaryMuscle() {
        return primaryMuscle;
    }

    public void setPrimaryMuscle(String primaryMuscle) {
        this.primaryMuscle = primaryMuscle;
    }
}