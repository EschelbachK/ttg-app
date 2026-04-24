package com.traintogain.backend.catalog.model;

import lombok.*;
import org.springframework.data.annotation.Id;
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

    private String name;

    private BodyRegion bodyRegion;

    private EquipmentType equipment;

    private String imageUrl;

    private String animationUrl;

    private Muscle primaryMuscle;

    private List<Muscle> secondaryMuscles;

    private ExerciseType exerciseType;

    private Difficulty difficulty;

    private MovementPattern movementPattern;

    private List<String> tags;

    private String thumbnail;

}