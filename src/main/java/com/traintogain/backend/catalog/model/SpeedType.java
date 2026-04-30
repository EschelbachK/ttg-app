package com.traintogain.backend.catalog.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SpeedType {

    CONTROLLED("Controlled"),
    EXPLOSIVE("Explosive"),
    SLOW("Slow"),
    STANDARD("Standard");

    private final String displayName;
}