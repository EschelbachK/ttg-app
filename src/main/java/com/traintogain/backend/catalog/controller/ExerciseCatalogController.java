package com.traintogain.backend.catalog.controller;

import com.traintogain.backend.catalog.dto.ExerciseCatalogDetailsResponse;
import com.traintogain.backend.catalog.dto.ExerciseCatalogResponse;
import com.traintogain.backend.catalog.dto.ExerciseFilterRequest;
import com.traintogain.backend.catalog.model.*;
import com.traintogain.backend.catalog.service.ExerciseCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/exercise-catalog")
@RequiredArgsConstructor
public class ExerciseCatalogController {

    private final ExerciseCatalogService service;

    @GetMapping("/categories")
    public List<BodyRegion> getCategories() {
        return List.of(BodyRegion.values());
    }

    @GetMapping("/equipment")
    public List<EquipmentType> getEquipmentTypes() {
        return List.of(EquipmentType.values());
    }

    @GetMapping("/patterns")
    public List<MovementPattern> getPatterns() {
        return List.of(MovementPattern.values());
    }

    @GetMapping("/health")
    public String health() {
        return "Exercise Catalog API running";
    }

    @GetMapping("/search")
    public List<ExerciseCatalogResponse> searchExercises(@RequestParam String q) {
        return service.searchExercises(q);
    }

    @GetMapping
    public List<ExerciseCatalogResponse> getExercises(
            @RequestParam(required = false) List<String> bodyRegion,
            @RequestParam(required = false) List<String> muscle,
            @RequestParam(required = false) List<String> equipment,
            @RequestParam(required = false) List<String> pattern,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) List<String> planes,
            @RequestParam(required = false) List<String> mechanics,
            @RequestParam(required = false) List<String> loadTypes,
            @RequestParam(required = false) List<String> lateralities,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort
    ) {

        ExerciseFilterRequest filter = ExerciseFilterRequest.builder()
                .bodyRegions(parse(bodyRegion, BodyRegion.class))
                .muscles(parse(muscle, Muscle.class))
                .equipment(parse(equipment, EquipmentType.class))
                .patterns(parse(pattern, MovementPattern.class))
                .tags(parse(tags, ExerciseTag.class))
                .planes(parse(planes, MovementPlane.class))
                .mechanics(parse(mechanics, MovementMechanic.class))
                .loadTypes(parse(loadTypes, LoadType.class))
                .lateralities(parse(lateralities, Laterality.class))
                .page(Math.max(page, 0))
                .size(Math.min(Math.max(size, 1), 100))
                .sort(sort)
                .build();

        return service.getExercises(filter);
    }

    @GetMapping("/{id}/alternatives")
    public List<ExerciseCatalogResponse> getAlternatives(
            @PathVariable String id,
            @RequestParam(defaultValue = "6") int limit
    ) {
        return service.getAlternatives(id, limit);
    }

    @GetMapping("/{id}")
    public ExerciseCatalogDetailsResponse getExercise(@PathVariable String id) {
        return service.getExercise(id);
    }

    private <T extends Enum<T>> List<T> parse(List<String> values, Class<T> clazz) {

        if (values == null || values.isEmpty()) {
            return List.of();
        }

        List<T> result = new ArrayList<>();

        for (String raw : values) {

            String normalized = normalize(raw);
            if (normalized == null) continue;

            try {
                result.add(Enum.valueOf(clazz, normalized));
            } catch (Exception ignored) {
            }
        }

        return result;
    }

    private String normalize(String value) {
        if (value == null) return null;

        String v = value.trim()
                .replace("-", "_")
                .replace(" ", "_")
                .toUpperCase();

        return v.isBlank() ? null : v;
    }
}