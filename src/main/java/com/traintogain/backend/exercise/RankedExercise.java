package com.traintogain.backend.exercise;

public class RankedExercise {

    private final TrainingExercise exercise;
    private final int score;

    public RankedExercise(TrainingExercise exercise, int score) {
        this.exercise = exercise;
        this.score = score;
    }

    public TrainingExercise getExercise() {
        return exercise;
    }

    public int score() {
        return score;
    }
}