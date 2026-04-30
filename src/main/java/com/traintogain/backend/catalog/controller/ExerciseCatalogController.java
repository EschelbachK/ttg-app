package com.traintogain.backend.catalog.controller;

import com.traintogain.backend.catalog.dto.ExerciseCatalogDetailsResponse;
import com.traintogain.backend.catalog.dto.ExerciseCatalogResponse;
import com.traintogain.backend.catalog.dto.ExerciseFilterRequest;
import com.traintogain.backend.catalog.model.*;
import com.traintogain.backend.catalog.service.ExerciseCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
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
        return Arrays.asList(EquipmentType.values());
    }

    @GetMapping("/patterns")
    public List<MovementPattern> getPatterns() {
        return Arrays.asList(MovementPattern.values());
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
                .bodyRegions(parseEnumList(bodyRegion, BodyRegion.class, "bodyRegion"))
                .muscles(parseEnumList(muscle, Muscle.class, "muscle"))
                .equipment(parseEnumList(equipment, EquipmentType.class, "equipment"))
                .patterns(parseEnumList(pattern, MovementPattern.class, "pattern"))
                .tags(parseEnumList(tags, ExerciseTag.class, "tags"))
                .planes(parseEnumList(planes, MovementPlane.class, "planes"))
                .mechanics(parseEnumList(mechanics, MovementMechanic.class, "mechanics"))
                .loadTypes(parseEnumList(loadTypes, LoadType.class, "loadTypes"))
                .lateralities(parseEnumList(lateralities, Laterality.class, "lateralities"))
                .page(page)
                .size(size)
                .sort(sort)
                .build();

        return service.getExercises(filter);
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

    private <T extends Enum<T>> List<T> parseEnumList(List<String> values, Class<T> clazz, String field) {
        if (values == null || values.isEmpty()) return List.of();
        try {
            return values.stream()
                    .map(v -> Enum.valueOf(clazz, v.trim().toUpperCase()))
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid " + field + ": " + values);
        }
    }
}