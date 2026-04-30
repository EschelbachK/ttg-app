package com.traintogain.backend.catalog.dto;

import com.traintogain.backend.catalog.model.*;
import com.traintogain.backend.exercise.ExerciseFamily;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExerciseCatalogResponse {

    private String id;

    private String name;

    private BodyRegion bodyRegion;

    private ExerciseFamily family;

    private MovementPattern movementPattern;

    @Builder.Default
    private List<EquipmentType> equipment = List.of();

    private Muscle primaryMuscle;

    private ExerciseType exerciseType;

    private Difficulty difficulty;

    @Builder.Default
    private List<String> tags = List.of();

    private String image;

    private String thumbnail;

    private String animation;
}