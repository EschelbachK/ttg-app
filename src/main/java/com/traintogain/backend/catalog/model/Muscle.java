package com.traintogain.backend.catalog.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Muscle {

    CHEST("Chest", BodyRegion.CHEST),
    PECTORALIS_MAJOR("Pectoralis Major", BodyRegion.CHEST),
    PECTORALIS_MINOR("Pectoralis Minor", BodyRegion.CHEST),

    BACK("Back", BodyRegion.BACK),
    LATISSIMUS_DORSI("Latissimus Dorsi", BodyRegion.BACK),
    TRAPEZIUS("Trapezius", BodyRegion.BACK),
    ERECTOR_SPINAE("Erector Spinae", BodyRegion.BACK),

    SHOULDERS("Shoulders", BodyRegion.SHOULDERS),
    DELTOID_ANTERIOR("Front Deltoid", BodyRegion.SHOULDERS),
    DELTOID_LATERAL("Lateral Deltoid", BodyRegion.SHOULDERS),
    DELTOID_POSTERIOR("Rear Deltoid", BodyRegion.SHOULDERS),

    BICEPS("Biceps", BodyRegion.ARMS),
    BICEPS_BRACHII("Biceps Brachii", BodyRegion.ARMS),
    TRICEPS("Triceps", BodyRegion.ARMS),
    TRICEPS_BRACHII("Triceps Brachii", BodyRegion.ARMS),
    FOREARMS("Forearms", BodyRegion.ARMS),
    BRACHIALIS("Brachialis", BodyRegion.ARMS),

    CORE("Core", BodyRegion.CORE),
    RECTUS_ABDOMINIS("Abs", BodyRegion.CORE),
    OBLIQUES("Obliques", BodyRegion.CORE),

    LEGS("Legs", BodyRegion.LEGS),
    QUADRICEPS("Quadriceps", BodyRegion.LEGS),
    HAMSTRINGS("Hamstrings", BodyRegion.LEGS),
    GLUTES("Glutes", BodyRegion.LEGS),
    GLUTEUS_MAXIMUS("Gluteus Maximus", BodyRegion.LEGS),
    GLUTEUS_MEDIUS("Gluteus Medius", BodyRegion.LEGS),
    CALVES("Calves", BodyRegion.LEGS),
    ILIOPSOAS("Hip Flexors", BodyRegion.LEGS);

    private final String displayName;
    private final BodyRegion bodyRegion;
}