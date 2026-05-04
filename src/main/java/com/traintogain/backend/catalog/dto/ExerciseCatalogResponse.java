package com.traintogain.backend.catalog.dto;

import com.traintogain.backend.exercise.ExerciseFamily;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExerciseCatalogResponse {

    private String id;

    private String name;

    private String bodyRegion;

    private ExerciseFamily family;

    private String movementPattern;

    @Builder.Default
    private List<String> equipment = List.of();

    private String primaryMuscle;

    private String exerciseType;

    private String difficulty;

    @Builder.Default
    private List<String> tags = List.of();

    private String image;

    private String thumbnail;

    private String animation;
}