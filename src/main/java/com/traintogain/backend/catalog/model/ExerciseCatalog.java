package com.traintogain.backend.catalog.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "exercise_catalog")
@CompoundIndexes({
        @CompoundIndex(name = "body_equipment_pattern_idx", def = "{'bodyRegion':1,'equipment':1,'movementPattern':1}"),
        @CompoundIndex(name = "body_idx", def = "{'bodyRegion':1}"),
        @CompoundIndex(name = "equipment_idx", def = "{'equipment':1}"),
        @CompoundIndex(name = "pattern_idx", def = "{'movementPattern':1}"),
        @CompoundIndex(name = "name_idx", def = "{'name':1}"),
        @CompoundIndex(name = "family_idx", def = "{'family':1}"),
        @CompoundIndex(name = "base_pattern_idx", def = "{'basePattern':1}")
})
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

    @Indexed
    private Muscle primaryMuscle;

    @Builder.Default
    private List<Muscle> secondaryMuscles = new ArrayList<>();

    @Indexed
    private ExerciseType exerciseType;

    @Indexed
    private Difficulty difficulty;

    @Indexed
    private MovementPattern movementPattern;

    // 🔥 NEW: SYSTEM CORE (UI + LOGIC SEPARATION)

    @Indexed
    private String family;

    @Indexed
    private String basePattern;

    // media
    @Builder.Default
    private String imageUrl = "";

    @Builder.Default
    private String animationUrl = "";

    @Builder.Default
    private String thumbnail = "";

    // UX content
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    private List<String> instructions = new ArrayList<>();

    @Builder.Default
    private List<String> tips = new ArrayList<>();

    @Builder.Default
    private List<String> commonMistakes = new ArrayList<>();
}