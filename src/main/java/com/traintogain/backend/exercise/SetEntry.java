package com.traintogain.backend.exercise;

public class SetEntry {

    private double weight;
    private int reps;

    public SetEntry(double weight, int reps) {
        this.weight = weight;
        this.reps = reps;
    }

    public double getWeight() {
        return weight;
    }

    public int getReps() {
        return reps;
    }
}