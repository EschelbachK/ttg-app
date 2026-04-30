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

    UPPER_BACK("Upper Back", BodyRegion.BACK),
    LOWER_BACK("Lower Back", BodyRegion.BACK),

    LATISSIMUS_DORSI("Latissimus Dorsi", BodyRegion.BACK),
    TRAPEZIUS("Trapezius", BodyRegion.BACK),
    RHOMBOIDS("Rhomboids", BodyRegion.BACK),
    ERECTOR_SPINAE("Erector Spinae", BodyRegion.BACK),

    SHOULDERS("Shoulders", BodyRegion.SHOULDERS),
    ANTERIOR_DELTOID("Front Deltoid", BodyRegion.SHOULDERS),
    LATERAL_DELTOID("Lateral Deltoid", BodyRegion.SHOULDERS),
    POSTERIOR_DELTOID("Rear Deltoid", BodyRegion.SHOULDERS),

    ROTATOR_CUFF("Rotator Cuff", BodyRegion.SHOULDERS),
    SERRATUS_ANTERIOR("Serratus Anterior", BodyRegion.CHEST),
    SCAPULAR_STABILIZERS("Scapular Stabilizers", BodyRegion.SHOULDERS),

    BICEPS("Biceps", BodyRegion.ARMS),
    BICEPS_BRACHII("Biceps Brachii", BodyRegion.ARMS),

    TRICEPS("Triceps", BodyRegion.ARMS),
    TRICEPS_BRACHII("Triceps Brachii", BodyRegion.ARMS),

    FOREARMS("Forearms", BodyRegion.ARMS),
    BRACHIALIS("Brachialis", BodyRegion.ARMS),

    CORE("Core", BodyRegion.CORE),
    RECTUS_ABDOMINIS("Abs", BodyRegion.CORE),
    TRANSVERSE_ABDOMINIS("Transverse Abdominis", BodyRegion.CORE),
    OBLIQUES("Obliques", BodyRegion.CORE),

    LEGS("Legs", BodyRegion.LEGS),
    QUADRICEPS("Quadriceps", BodyRegion.LEGS),
    HAMSTRINGS("Hamstrings", BodyRegion.LEGS),

    GLUTES("Glutes", BodyRegion.LEGS),
    GLUTEUS_MAXIMUS("Gluteus Maximus", BodyRegion.LEGS),
    GLUTEUS_MEDIUS("Gluteus Medius", BodyRegion.LEGS),

    ADDUCTORS("Adductors", BodyRegion.LEGS),

    CALVES("Calves", BodyRegion.LEGS),

    GASTROCNEMIUS("Gastrocnemius", BodyRegion.LEGS),

    SOLEUS("Soleus", BodyRegion.LEGS),

    HIP_FLEXORS("Hip Flexors", BodyRegion.LEGS);

    private final String displayName;
    private final BodyRegion bodyRegion;
}