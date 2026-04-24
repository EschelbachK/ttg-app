package com.traintogain.backend.catalog.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "exercise_catalog")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseCatalog {

    @Id
    private String id;

    @Indexed
    private String name;

    @Indexed
    private BodyRegion bodyRegion;

    @Indexed
    private EquipmentType equipment;

    private String imageUrl;

    private String animationUrl;

    @Indexed
    private Muscle primaryMuscle;

    private List<Muscle> secondaryMuscles;

    private ExerciseType exerciseType;

    private Difficulty difficulty;

    @Indexed
    private MovementPattern movementPattern;

    private List<String> tags;

    private String thumbnail;

}