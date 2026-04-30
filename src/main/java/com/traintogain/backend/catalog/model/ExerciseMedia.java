package com.traintogain.backend.catalog.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseMedia {

    @Builder.Default
    private String imageFile = null;

    @Builder.Default
    private String thumbnailFile = null;

    @Builder.Default
    private String animationFile = null;
}