package com.traintogain.backend.catalog.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RiskType {

    LOWER_BACK_STRAIN("lower_back_strain"),
    SHOULDER_IMPINGEMENT("shoulder_impingement"),
    WRIST_STRESS("wrist_stress"),
    NECK_STRAIN("neck_strain"),
    MUSCLE_STRAIN("muscle_strain"),

    UNKNOWN("unknown");

    private final String key;

    @JsonCreator
    public static RiskType from(String value) {
        if (value == null) return UNKNOWN;

        String normalized = value.trim().toLowerCase();

        for (RiskType type : values()) {
            if (type.key.equals(normalized) || type.name().equalsIgnoreCase(normalized)) {
                return type;
            }
        }

        return UNKNOWN;
    }

    @JsonValue
    public String toJson() {
        return key;
    }
}