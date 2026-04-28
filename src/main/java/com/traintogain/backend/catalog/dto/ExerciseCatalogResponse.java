package com.traintogain.backend.catalog.dto;

import com.traintogain.backend.catalog.model.*;
import com.traintogain.backend.exercise.BasePatternRegistry;
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
    private BasePatternRegistry basePattern;
    private EquipmentType equipment;
    private Muscle primaryMuscle;
    private ExerciseType exerciseType;
    private Difficulty difficulty;
    private List<String> tags;
    private ExerciseMedia media;
}