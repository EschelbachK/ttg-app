package com.traintogain.backend.catalog.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MovementPattern {

    // Upper Body
    PUSH("Push", MovementCategory.UPPER_BODY),
    PULL("Pull", MovementCategory.UPPER_BODY),

    // Lower Body
    SQUAT("Squat", MovementCategory.LOWER_BODY),
    HINGE("Hinge", MovementCategory.LOWER_BODY),
    LUNGE("Lunge", MovementCategory.LOWER_BODY),
    KNEE_FLEXION("Knee Flexion", MovementCategory.LOWER_BODY),

    // Core
    CORE("Core", MovementCategory.CORE),

    // Functional / Carry
    CARRY("Carry", MovementCategory.FULL_BODY),

    // Conditioning
    CONDITIONING("Conditioning", MovementCategory.FULL_BODY);

    private final String displayName;
    private final MovementCategory category;
}