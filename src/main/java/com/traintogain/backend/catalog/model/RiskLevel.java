package com.traintogain.backend.catalog.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RiskLevel {

    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String displayName;
}