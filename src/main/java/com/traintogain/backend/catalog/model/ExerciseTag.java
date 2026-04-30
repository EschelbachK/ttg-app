package com.traintogain.backend.catalog.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExerciseTag {

    // Structure
    COMPOUND("Compound", TagCategory.STRUCTURE),
    ISOLATION("Isolation", TagCategory.STRUCTURE),

    // Goal
    STRENGTH("Strength", TagCategory.GOAL),
    HYPERTROPHY("Hypertrophy", TagCategory.GOAL),
    ENDURANCE("Endurance", TagCategory.GOAL),

    // Level
    BEGINNER_FRIENDLY("Beginner Friendly", TagCategory.LEVEL),
    ADVANCED("Advanced", TagCategory.LEVEL),

    // Function
    CORE_STABILITY("Core Stability", TagCategory.FUNCTION),
    MOBILITY("Mobility", TagCategory.FUNCTION),
    ATHLETIC("Athletic", TagCategory.FUNCTION),

    // Rehab
    REHAB("Rehab", TagCategory.REHAB);

    private final String displayName;
    private final TagCategory category;
}