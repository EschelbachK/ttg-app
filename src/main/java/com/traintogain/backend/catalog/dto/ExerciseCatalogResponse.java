package com.traintogain.backend.catalog.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExerciseCatalogResponse {

    private String id;

    private String name;

    private String imageUrl;

}