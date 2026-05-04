package com.traintogain.backend.catalog.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RiskType {

    UNKNOWN("unknown"),
    LOWER_BACK_STRAIN("lower_back_strain"),
    LOWER_BACK_SAGGING("lower_back_sagging"),
    LOWER_BACK_STRESS("lower_back_stress"),
    LOWER_BACK_ROUNDING("lower_back_rounding"),
    LOWER_BACK_OVEREXTENSION("lower_back_overextension"),
    SHOULDER_IMPINGEMENT("shoulder_impingement"),
    SHOULDER_STRAIN("shoulder_strain"),
    SHOULDER_STRESS("shoulder_stress"),
    SHOULDER_OVERSTRETCH("shoulder_overstretch"),
    WRIST_STRESS("wrist_stress"),
    WRIST_STRAIN("wrist_strain"),
    NECK_STRAIN("neck_strain"),
    NECK_PULL("neck_pull"),
    MUSCLE_STRAIN("muscle_strain"),
    KNEE_VALGUS("knee_valgus"),
    KNEE_OVERLOAD("knee_overload"),
    KNEE_STRESS("knee_stress"),
    ANKLE_INSTABILITY("ankle_instability"),
    ELBOW_STRAIN("elbow_strain"),
    ELBOW_STRESS("elbow_stress"),
    BAR_PATH_INSTABILITY("bar_path_instability"),
    HIP_SAGGING("hip_sagging"),
    BALANCE_LOSS("balance_loss"),
    LOSS_OF_CONTROL("loss_of_control"),
    IMPROPER_FORM("improper_form"),
    ROUNDING_BACK("rounding_back"),
    SWINGING("swinging"),
    IMBALANCE("imbalance");

    private final String key;

    @JsonCreator
    public static RiskType from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("risk type is null or blank");
        }

        String normalized = value.trim().toLowerCase();

        for (RiskType type : values()) {
            if (type.key.equals(normalized) || type.name().equalsIgnoreCase(normalized)) {
                return type;
            }
        }

        throw new IllegalArgumentException("invalid risk type: " + value);
    }

    @JsonValue
    public String toJson() {
        return key;
    }
}