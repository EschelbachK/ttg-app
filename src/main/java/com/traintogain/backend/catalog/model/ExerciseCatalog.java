package com.traintogain.backend.catalog.model;

import com.traintogain.backend.exercise.BasePatternRegistry;
import com.traintogain.backend.exercise.ExerciseFamily;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "exercise_catalog")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "body_region_idx", def = "{'bodyRegion':1}"),
        @CompoundIndex(name = "pattern_idx", def = "{'movementPattern':1}"),
        @CompoundIndex(name = "family_idx", def = "{'family':1}"),
        @CompoundIndex(name = "base_pattern_idx", def = "{'basePattern':1}")
})
public class ExerciseCatalog {

    @Id
    private String id;
    private String name;
    private BodyRegion bodyRegion;
    private ExerciseFamily family;
    private MovementPattern movementPattern;
    private BasePatternRegistry basePattern;
    private EquipmentType equipment;
    private Muscle primaryMuscle;
    @Builder.Default
    private List<Muscle> secondaryMuscles = new ArrayList<>();
    @Builder.Default
    private List<Muscle> stabilizers = new ArrayList<>();
    private ExerciseType exerciseType;
    private Difficulty difficulty;
    private SpeedType speedType;
    @Builder.Default
    private List<ExerciseTag> tags = new ArrayList<>();
    private ExerciseMedia media;
    private Execution execution;
    private Progression progression;
    private Safety safety;
    @Builder.Default
    private List<String> instructions = new ArrayList<>();
    @Builder.Default
    private List<String> tips = new ArrayList<>();
    @Builder.Default
    private List<String> commonMistakes = new ArrayList<>();

    public void ensureSafeDefaults() {
        if (secondaryMuscles == null) secondaryMuscles = new ArrayList<>();
        if (stabilizers == null) stabilizers = new ArrayList<>();
        if (tags == null) tags = new ArrayList<>();
        if (instructions == null) instructions = new ArrayList<>();
        if (tips == null) tips = new ArrayList<>();
        if (commonMistakes == null) commonMistakes = new ArrayList<>();
        if (speedType == null) speedType = SpeedType.CONTROLLED;
    }
}