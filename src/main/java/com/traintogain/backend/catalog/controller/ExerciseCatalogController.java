package com.traintogain.backend.catalog.controller;

import com.traintogain.backend.catalog.dto.ExerciseCatalogDetailsResponse;
import com.traintogain.backend.catalog.dto.ExerciseCatalogResponse;
import com.traintogain.backend.catalog.model.BodyRegion;
import com.traintogain.backend.catalog.model.EquipmentType;
import com.traintogain.backend.catalog.service.ExerciseCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercise-catalog")
@RequiredArgsConstructor
public class ExerciseCatalogController {

    private final ExerciseCatalogService service;

    @GetMapping("/body-regions")
    public List<BodyRegion> getBodyRegions() {
        return service.getBodyRegions();
    }

    @GetMapping
    public List<ExerciseCatalogResponse> getExercises(
            @RequestParam(required = false) BodyRegion bodyRegion,
            @RequestParam(required = false) EquipmentType equipment
    ) {
        return service.getExercises(bodyRegion, equipment);
    }

    @GetMapping("/search")
    public List<ExerciseCatalogResponse> searchExercises(
            @RequestParam String q
    ) {
        return service.searchExercises(q);
    }

    @GetMapping("/{id}")
    public ExerciseCatalogDetailsResponse getExercise(
            @PathVariable String id
    ) {
        return service.getExercise(id);
    }
}