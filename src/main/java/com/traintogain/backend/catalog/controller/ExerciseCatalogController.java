package com.traintogain.backend.catalog.controller;

import com.traintogain.backend.catalog.dto.ExerciseCatalogDetailsResponse;
import com.traintogain.backend.catalog.dto.ExerciseCatalogResponse;
import com.traintogain.backend.catalog.model.BodyRegion;
import com.traintogain.backend.catalog.model.EquipmentType;
import com.traintogain.backend.catalog.model.MovementPattern;
import com.traintogain.backend.catalog.service.ExerciseCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

    @GetMapping("/equipment")
    public List<EquipmentType> getEquipmentTypes() {
        return Arrays.asList(EquipmentType.values());
    }

    @GetMapping("/patterns")
    public List<MovementPattern> getPatterns() {
        return Arrays.asList(MovementPattern.values());
    }

    @GetMapping
    public List<ExerciseCatalogResponse> getExercises(
            @RequestParam(required = false) BodyRegion bodyRegion,
            @RequestParam(required = false) EquipmentType equipment,
            @RequestParam(required = false) MovementPattern pattern,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort
    ) {
        return service.getExercises(bodyRegion, equipment, pattern, tags, page, size, sort);
    }

    @GetMapping("/all")
    public List<ExerciseCatalogResponse> getAllExercises() {
        return service.getExercises(null, null, null, null, 0, Integer.MAX_VALUE, null);
    }

    @GetMapping("/search")
    public List<ExerciseCatalogResponse> searchExercises(@RequestParam String q) {
        return service.searchExercises(q);
    }

    @GetMapping("/{id}")
    public ExerciseCatalogDetailsResponse getExercise(@PathVariable String id) {
        return service.getExercise(id);
    }

    @GetMapping("/health")
    public String health() {
        return "Exercise Catalog API running";
    }
}