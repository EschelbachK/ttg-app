package com.traintogain.backend.catalog.controller;

import com.traintogain.backend.catalog.model.BodyRegion;
import com.traintogain.backend.catalog.model.ExerciseCatalog;
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
    public List<ExerciseCatalog> getExercises(
            @RequestParam BodyRegion bodyRegion
    ) {
        return service.getExercisesByRegion(bodyRegion);
    }
}