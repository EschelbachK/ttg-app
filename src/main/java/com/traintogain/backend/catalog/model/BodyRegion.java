package com.traintogain.backend.catalog.model;

public enum BodyRegion {

    CHEST("Chest"),
    BACK("Back"),
    SHOULDERS("Shoulders"),
    ARMS("Arms"),
    LEGS("Legs"),
    CORE("Core"),
    FULL_BODY("Full Body"),

    UPPER_BODY("Upper Body"),
    LOWER_BODY("Lower Body");

    private final String displayName;

    BodyRegion(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}