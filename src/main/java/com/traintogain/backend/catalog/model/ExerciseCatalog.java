package com.traintogain.backend.catalog.model;

import com.traintogain.backend.exercise.BaseMovementPattern;
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

    private BaseMovementPattern basePattern;

    private MovementPlane movementPlane;

    private MovementMechanic mechanic;

    private LoadType loadType;

    private Laterality laterality;

    @Builder.Default
    private List<EquipmentType> equipment = new ArrayList<>();

    private Muscle primaryMuscle;

    @Builder.Default
    private List<Muscle> secondaryMuscles = new ArrayList<>();

    @Builder.Default
    private List<Muscle> stabilizers = new ArrayList<>();

    private ExerciseType exerciseType;

    private Difficulty difficulty;

    @Builder.Default
    private List<ExerciseTag> tags = new ArrayList<>();

    private ExerciseMedia media;

    private Execution execution;

    private Progression progression;

    private Safety safety;

    private String instructions;

    @Builder.Default
    private List<String> tips = new ArrayList<>();

    @Builder.Default
    private List<String> commonMistakes = new ArrayList<>();
}