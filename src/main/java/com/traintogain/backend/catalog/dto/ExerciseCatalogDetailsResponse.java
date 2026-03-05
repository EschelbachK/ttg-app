package com.traintogain.backend.catalog.dto;

import com.traintogain.backend.catalog.model.BodyRegion;
import com.traintogain.backend.catalog.model.EquipmentType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExerciseCatalogDetailsResponse {

    private String id;

    private String name;

    private String imageUrl;

    private String animationUrl;

    private BodyRegion bodyRegion;

    private EquipmentType equipment;

}