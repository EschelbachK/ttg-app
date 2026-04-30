package com.traintogain.backend.catalog.dto;

import com.traintogain.backend.catalog.model.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExerciseFilterRequest {

    @Builder.Default
    private List<BodyRegion> bodyRegions = List.of();

    @Builder.Default
    private List<Muscle> muscles = List.of();

    @Builder.Default
    private List<EquipmentType> equipment = List.of();

    @Builder.Default
    private List<MovementPattern> patterns = List.of();

    @Builder.Default
    private List<ExerciseTag> tags = List.of();

    @Builder.Default
    private List<MovementPlane> planes = List.of();

    @Builder.Default
    private List<MovementMechanic> mechanics = List.of();

    @Builder.Default
    private List<LoadType> loadTypes = List.of();

    @Builder.Default
    private List<Laterality> lateralities = List.of();

    private int page;

    private int size;

    private String sort;
}