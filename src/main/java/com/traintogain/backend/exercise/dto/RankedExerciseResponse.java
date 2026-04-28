package com.traintogain.backend.exercise.dto;

import com.traintogain.backend.catalog.model.ExerciseCatalog;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankedExerciseResponse {

    private final String id;
    private final String name;
    private final String imageUrl;
    private final int score;

    public static RankedExerciseResponse from(ExerciseCatalog e, int score) {
        return RankedExerciseResponse.builder()
                .id(e.getId())
                .name(e.getName())
                .imageUrl(e.getMedia() != null ? e.getMedia().getImage() : null)
                .score(score)
                .build();
    }
}