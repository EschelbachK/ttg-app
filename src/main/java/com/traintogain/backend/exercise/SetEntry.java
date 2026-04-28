package com.traintogain.backend.exercise;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SetEntry {

    private double weight;
    private int repetitions;

    public SetEntry() {}

    public SetEntry(double weight, int repetitions) {
        this.weight = weight;
        this.repetitions = repetitions;
    }

}