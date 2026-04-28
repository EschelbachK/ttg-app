package com.traintogain.backend.catalog.dto;

import com.traintogain.backend.catalog.model.*;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class ExerciseFilterRequest {
    private BodyRegion bodyRegion;
    private Muscle muscle;
    private EquipmentType equipment;
    private MovementPattern pattern;
    private List<ExerciseTag> tags;
    private int page;
    private int size;
    private String sort;
}