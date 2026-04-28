package com.traintogain.backend.catalog.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseMedia {
    private String image;
    private String thumbnail;
    private String animation;
}