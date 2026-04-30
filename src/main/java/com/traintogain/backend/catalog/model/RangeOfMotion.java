package com.traintogain.backend.catalog.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RangeOfMotion {

    FULL("Full"),
    PARTIAL("Partial"),
    STATIC("Static");

    private final String displayName;
}