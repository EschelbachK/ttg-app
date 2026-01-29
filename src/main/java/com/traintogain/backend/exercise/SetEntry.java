package com.traintogain.backend.exercise;

public class SetEntry {

    private int order;        // Satz 1, 2, 3 …
    private double weight;    // kg
    private int repetitions; // Wiederholungen
    private boolean completed;

    public SetEntry() {
    }

    public SetEntry(int order, double weight, int repetitions) {
        this.order = order;
        this.weight = weight;
        this.repetitions = repetitions;
        this.completed = false;
    }

    public int getOrder() {
        return order;
    }

    public double getWeight() {
        return weight;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
