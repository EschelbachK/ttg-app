package com.traintogain.backend.catalog.dto;

import com.traintogain.backend.exercise.ExerciseFamily;
import com.traintogain.backend.catalog.model.Execution;
import com.traintogain.backend.catalog.model.Progression;
import com.traintogain.backend.catalog.model.Safety;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExerciseCatalogDetailsResponse {

    private String id;

    private String name;

    private String bodyRegion;

    private ExerciseFamily family;

    private String movementPattern;

    @Builder.Default
    private List<String> equipment = List.of();

    private String primaryMuscle;

    @Builder.Default
    private List<String> secondaryMuscles = List.of();

    @Builder.Default
    private List<String> stabilizers = List.of();

    private String exerciseType;

    private String difficulty;

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