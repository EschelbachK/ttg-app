package com.traintogain.backend.catalog.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MovementPattern {

    PUSH("Push", MovementCategory.UPPER_BODY),
    PULL("Pull", MovementCategory.UPPER_BODY),

    SQUAT("Squat", MovementCategory.LOWER_BODY),
    HINGE("Hinge", MovementCategory.LOWER_BODY),
    LUNGE("Lunge", MovementCategory.LOWER_BODY),
    KNEE_FLEXION("Knee Flexion", MovementCategory.LOWER_BODY),
    HIP_FLEXION("Hip Flexion", MovementCategory.LOWER_BODY),

    CORE("Core", MovementCategory.CORE),

    CARRY("Carry", MovementCategory.FULL_BODY),
    CONDITIONING("Conditioning", MovementCategory.FULL_BODY),

    ANTI_EXTENSION("Anti Extension", MovementCategory.CORE),
    ANTI_LATERAL_FLEXION("Anti Lateral Flexion", MovementCategory.CORE),
    ROTATION("Rotation", MovementCategory.CORE),
    SPINAL_FLEXION("Spinal Flexion", MovementCategory.CORE),

    HORIZONTAL_PUSH("Horizontal Push", MovementCategory.UPPER_BODY),
    VERTICAL_PUSH("Vertical Push", MovementCategory.UPPER_BODY),

    HORIZONTAL_PULL("Horizontal Pull", MovementCategory.UPPER_BODY),
    VERTICAL_PULL("Vertical Pull", MovementCategory.UPPER_BODY),

    ELBOW_FLEXION("Elbow Flexion", MovementCategory.UPPER_BODY),
    ELBOW_EXTENSION("Elbow Extension", MovementCategory.UPPER_BODY),

    KNEE_EXTENSION("Knee Extension", MovementCategory.LOWER_BODY),
    PLANTAR_FLEXION("Plantar Flexion", MovementCategory.LOWER_BODY);

    private final String displayName;
    private final MovementCategory category;
}