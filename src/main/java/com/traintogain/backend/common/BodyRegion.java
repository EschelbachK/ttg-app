package com.traintogain.backend.common;

public enum BodyRegion {

    BRUST("Brust"),
    RUECKEN("Rücken"),
    BEINE("Beine"),
    SCHULTERN("Schultern"),
    BIZEPS("Bizeps"),
    TRIZEPS("Trizeps"),
    BAUCH("Bauch"),
    NACKEN("Nacken"),
    CARDIO("Cardio"),
    GANZKOERPER("Ganzkörper");

    private final String displayName;

    BodyRegion(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
