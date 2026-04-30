package com.traintogain.backend.catalog.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExerciseType {

    STRENGTH("Strength"),
    HYPERTROPHY("Hypertrophy"),
    ENDURANCE("Endurance"),
    CONDITIONING("Conditioning"),
    REHAB("Rehab");

    private final String displayName;
}