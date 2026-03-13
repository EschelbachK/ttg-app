package com.traintogain.backend.catalog.controller;

import com.traintogain.backend.catalog.dto.ExerciseCatalogDetailsResponse;
import com.traintogain.backend.catalog.dto.ExerciseCatalogResponse;
import com.traintogain.backend.catalog.model.BodyRegion;
import com.traintogain.backend.catalog.model.EquipmentType;
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


    /**
     * ------------------------------------------------
     * BODY REGIONS
     * ------------------------------------------------
     */

    @GetMapping("/body-regions")
    public List<BodyRegion> getBodyRegions() {
        return service.getBodyRegions();
    }


    /**
     * ------------------------------------------------
     * EQUIPMENT TYPES
     * ------------------------------------------------
     */

    @GetMapping("/equipment")
    public List<EquipmentType> getEquipmentTypes() {
        return Arrays.asList(EquipmentType.values());
    }


    /**
     * ------------------------------------------------
     * GET EXERCISES (FILTER)
     * ------------------------------------------------
     */

    @GetMapping
    public List<ExerciseCatalogResponse> getExercises(
            @RequestParam(required = false) BodyRegion bodyRegion,
            @RequestParam(required = false) EquipmentType equipment,
            @RequestParam(required = false) String sort
    ) {
        return service.getExercises(bodyRegion, equipment, sort);
    }


    /**
     * ------------------------------------------------
     * GET ALL EXERCISES
     * ------------------------------------------------
     * Wird vom Flutter Catalog genutzt
     */

    @GetMapping("/all")
    public List<ExerciseCatalogResponse> getAllExercises() {
        return service.getExercises(null, null, null);
    }


    /**
     * ------------------------------------------------
     * SEARCH
     * ------------------------------------------------
     */

    @GetMapping("/search")
    public List<ExerciseCatalogResponse> searchExercises(
            @RequestParam String q
    ) {
        return service.searchExercises(q);
    }


    /**
     * ------------------------------------------------
     * GET SINGLE EXERCISE
     * ------------------------------------------------
     */

    @GetMapping("/{id}")
    public ExerciseCatalogDetailsResponse getExercise(
            @PathVariable String id
    ) {
        return service.getExercise(id);
    }


    /**
     * ------------------------------------------------
     * HEALTH CHECK
     * ------------------------------------------------
     * Hilft beim Debuggen im Browser
     */

    @GetMapping("/health")
    public String health() {
        return "Exercise Catalog API running";
    }

}