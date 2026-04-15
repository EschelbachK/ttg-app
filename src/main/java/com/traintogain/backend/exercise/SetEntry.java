package com.traintogain.backend.exercise;

public class SetEntry {

    private double weight;
    private int repetitions;

    public SetEntry() {}

    public SetEntry(double weight, int repetitions) {
        this.weight = weight;
        this.repetitions = repetitions;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }
}