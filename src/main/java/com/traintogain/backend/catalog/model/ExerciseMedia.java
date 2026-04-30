package com.traintogain.backend.catalog.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseMedia {

    @Builder.Default
    private String imageFile = "";

    @Builder.Default
    private String thumbnailFile = "";

    @Builder.Default
    private String animationFile = "";
}