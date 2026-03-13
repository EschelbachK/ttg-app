package com.traintogain.backend.catalog.dto;

import com.traintogain.backend.catalog.model.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExerciseCatalogResponse {

    private String id;

    private String name;

    private String imageUrl;

    private String animationUrl;

    private BodyRegion bodyRegion;

    private EquipmentType equipment;

    private Muscle primaryMuscle;

    private List<Muscle> secondaryMuscles;

    private ExerciseType exerciseType;

    private Difficulty difficulty;

}