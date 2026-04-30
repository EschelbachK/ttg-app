package com.traintogain.backend.catalog.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EquipmentType {

    // Free Weights
    BARBELL("Barbell", EquipmentCategory.FREE_WEIGHTS),
    DUMBBELL("Dumbbell", EquipmentCategory.FREE_WEIGHTS),
    KETTLEBELL("Kettlebell", EquipmentCategory.FREE_WEIGHTS),

    // Machines
    MACHINE("Machine", EquipmentCategory.MACHINE),
    CABLE("Cable Machine", EquipmentCategory.MACHINE),

    // Bodyweight
    BODYWEIGHT("Bodyweight", EquipmentCategory.BODYWEIGHT),
    PULLUP_BAR("Pull-up Bar", EquipmentCategory.BODYWEIGHT),
    DIP_BAR("Dip Bar", EquipmentCategory.BODYWEIGHT),

    // Accessories
    RESISTANCE_BAND("Resistance Band", EquipmentCategory.ACCESSORY),
    AB_WHEEL("Ab Wheel", EquipmentCategory.ACCESSORY),
    BENCH("Bench", EquipmentCategory.ACCESSORY),

    // Cardio
    CARDIO_MACHINE("Cardio Machine", EquipmentCategory.CARDIO);

    private final String displayName;
    private final EquipmentCategory category;
}