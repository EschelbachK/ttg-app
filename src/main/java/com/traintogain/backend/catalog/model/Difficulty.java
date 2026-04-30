package com.traintogain.backend.catalog.model;

import lombok.Getter;

@Getter
public enum Difficulty {

    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced");

    private final String displayName;

    Difficulty(String displayName) {
        this.displayName = displayName;
    }

}