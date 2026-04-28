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
        return service.getCategories();
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
            @RequestParam(required = false) String bodyRegion,
            @RequestParam(required = false) String muscle,
            @RequestParam(required = false) String equipment,
            @RequestParam(required = false) String pattern,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort
    ) {
        ExerciseFilterRequest filter = ExerciseFilterRequest.builder()
                .bodyRegion(parseEnum(bodyRegion, BodyRegion.class, "bodyRegion"))
                .muscle(parseEnum(muscle, Muscle.class, "muscle"))
                .equipment(parseEnum(equipment, EquipmentType.class, "equipment"))
                .pattern(parseEnum(pattern, MovementPattern.class, "pattern"))
                .tags(parseEnumList(tags, ExerciseTag.class, "tags"))
                .page(page)
                .size(size)
                .sort(sort)
                .build();

        return service.getExercises(filter);
    }

    @GetMapping("/all")
    public List<ExerciseCatalogResponse> getAllExercises() {
        ExerciseFilterRequest filter = ExerciseFilterRequest.builder()
                .page(0)
                .size(Integer.MAX_VALUE)
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

    private <T extends Enum<T>> T parseEnum(String value, Class<T> clazz, String field) {
        if (value == null || value.isBlank()) return null;
        try {
            return Enum.valueOf(clazz, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid " + field + ": " + value);
        }
    }

    private <T extends Enum<T>> List<T> parseEnumList(List<String> values, Class<T> clazz, String field) {
        if (values == null || values.isEmpty()) return null;
        try {
            return values.stream().map(v -> Enum.valueOf(clazz, v.toUpperCase())).toList();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid " + field + ": " + values);
        }
    }
}