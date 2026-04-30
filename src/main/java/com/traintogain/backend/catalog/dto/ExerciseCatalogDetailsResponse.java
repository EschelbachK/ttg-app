package com.traintogain.backend.catalog.dto;

import com.traintogain.backend.catalog.model.*;
import com.traintogain.backend.exercise.ExerciseFamily;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExerciseCatalogDetailsResponse {

    private String id;

    private String name;

    private BodyRegion bodyRegion;

    private ExerciseFamily family;

    private MovementPattern movementPattern;

    @Builder.Default
    private List<EquipmentType> equipment = List.of();

    private Muscle primaryMuscle;

    @Builder.Default
    private List<Muscle> secondaryMuscles = List.of();

    @Builder.Default
    private List<Muscle> stabilizers = List.of();

    private ExerciseType exerciseType;

    private Difficulty difficulty;

    @Builder.Default
    private List<String> tags = List.of();

    private String image;

    private String thumbnail;

    private String animation;

    private Execution execution;

    private Progression progression;

    private Safety safety;

    @Builder.Default
    private List<String> instructions = List.of();

    @Builder.Default
    private List<String> tips = List.of();

    @Builder.Default
    private List<String> commonMistakes = List.of();
}