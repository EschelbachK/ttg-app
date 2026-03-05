package com.traintogain.backend.catalog.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
}