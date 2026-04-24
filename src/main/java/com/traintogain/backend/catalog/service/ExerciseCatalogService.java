package com.traintogain.backend.catalog.service;

import com.traintogain.backend.catalog.dto.ExerciseCatalogDetailsResponse;
import com.traintogain.backend.catalog.dto.ExerciseCatalogResponse;
import com.traintogain.backend.catalog.model.*;
import com.traintogain.backend.catalog.repository.ExerciseCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseCatalogService {

    private final ExerciseCatalogRepository repository;

    public List<BodyRegion> getBodyRegions() {
        return Arrays.asList(BodyRegion.values());
    }

    public List<ExerciseCatalogResponse> getExercises(
            BodyRegion bodyRegion,
            EquipmentType equipment,
            MovementPattern pattern,
            List<String> tags,
            int page,
            int size,
            String sort
    ) {

        List<ExerciseCatalog> exercises = repository.findAll(PageRequest.of(page, size)).getContent();

        if (bodyRegion != null) {
            exercises = exercises.stream().filter(e -> e.getBodyRegion() == bodyRegion).toList();
        }

        if (equipment != null) {
            exercises = exercises.stream().filter(e -> e.getEquipment() == equipment).toList();
        }

        if (pattern != null) {
            exercises = exercises.stream().filter(e -> e.getMovementPattern() == pattern).toList();
        }

        if (tags != null && !tags.isEmpty()) {
            exercises = exercises.stream()
                    .filter(e -> e.getTags() != null && e.getTags().stream().anyMatch(tags::contains))
                    .toList();
        }

        if ("name_desc".equals(sort)) {
            exercises.sort(Comparator.comparing(ExerciseCatalog::getName).reversed());
        } else {
            exercises.sort(Comparator.comparing(ExerciseCatalog::getName));
        }

        return exercises.stream().map(e -> ExerciseCatalogResponse.builder()
                .id(e.getId())
                .name(e.getName())
                .imageUrl(e.getImageUrl())
                .animationUrl(e.getAnimationUrl())
                .thumbnail(e.getThumbnail())
                .bodyRegion(e.getBodyRegion())
                .equipment(e.getEquipment())
                .primaryMuscle(e.getPrimaryMuscle())
                .secondaryMuscles(e.getSecondaryMuscles())
                .exerciseType(e.getExerciseType())
                .difficulty(e.getDifficulty())
                .movementPattern(e.getMovementPattern())
                .tags(e.getTags())
                .build()).toList();
    }

    public List<ExerciseCatalogResponse> searchExercises(String query) {

        String q = query.toLowerCase();

        return repository.findAll().stream()
                .filter(e ->
                        e.getName().toLowerCase().contains(q)
                                || (e.getTags() != null && e.getTags().stream().anyMatch(t -> t.toLowerCase().contains(q)))
                                || e.getPrimaryMuscle().name().toLowerCase().contains(q)
                )
                .map(e -> ExerciseCatalogResponse.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .imageUrl(e.getImageUrl())
                        .animationUrl(e.getAnimationUrl())
                        .thumbnail(e.getThumbnail())
                        .bodyRegion(e.getBodyRegion())
                        .equipment(e.getEquipment())
                        .primaryMuscle(e.getPrimaryMuscle())
                        .secondaryMuscles(e.getSecondaryMuscles())
                        .exerciseType(e.getExerciseType())
                        .difficulty(e.getDifficulty())
                        .movementPattern(e.getMovementPattern())
                        .tags(e.getTags())
                        .build())
                .toList();
    }

    public ExerciseCatalogDetailsResponse getExercise(String id) {

        var e = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        return ExerciseCatalogDetailsResponse.builder()
                .id(e.getId())
                .name(e.getName())
                .imageUrl(e.getImageUrl())
                .animationUrl(e.getAnimationUrl())
                .thumbnail(e.getThumbnail())
                .bodyRegion(e.getBodyRegion())
                .equipment(e.getEquipment())
                .primaryMuscle(e.getPrimaryMuscle())
                .secondaryMuscles(e.getSecondaryMuscles())
                .exerciseType(e.getExerciseType())
                .difficulty(e.getDifficulty())
                .movementPattern(e.getMovementPattern())
                .tags(e.getTags())
                .build();
    }
}