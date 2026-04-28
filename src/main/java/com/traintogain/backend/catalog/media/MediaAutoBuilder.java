package com.traintogain.backend.catalog.media;

import com.traintogain.backend.catalog.model.ExerciseMedia;

public class MediaAutoBuilder {

    public static ExerciseMedia build(String id) {
        return ExerciseMedia.builder()
                .image(id + ".jpg")
                .thumbnail(id + "_thumb.jpg")
                .animation(id + ".gif")
                .build();
    }
}