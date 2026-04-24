package com.traintogain.backend.catalog.service;

import com.traintogain.backend.catalog.dto.ExerciseCatalogDetailsResponse;
import com.traintogain.backend.catalog.dto.ExerciseCatalogResponse;
import com.traintogain.backend.catalog.model.*;
import com.traintogain.backend.catalog.repository.ExerciseCatalogRepository;
import lombok.RequiredArgsConstructor;
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
            String sort
    ) {

        List<ExerciseCatalog> exercises;

        if (bodyRegion != null && equipment != null && pattern != null) {
            exercises = repository.findByBodyRegionAndEquipmentAndMovementPattern(bodyRegion, equipment, pattern);
        } else if (bodyRegion != null && equipment != null) {
            exercises = repository.findByBodyRegionAndEquipment(bodyRegion, equipment);
        } else if (bodyRegion != null && pattern != null) {
            exercises = repository.findByBodyRegionAndMovementPattern(bodyRegion, pattern);
        } else if (equipment != null && pattern != null) {
            exercises = repository.findByEquipmentAndMovementPattern(equipment, pattern);
        } else if (bodyRegion != null) {
            exercises = repository.findByBodyRegion(bodyRegion);
        } else if (equipment != null) {
            exercises = repository.findByEquipment(equipment);
        } else if (pattern != null) {
            exercises = repository.findByMovementPattern(pattern);
        } else {
            exercises = repository.findAll();
        }

        if ("name_desc".equals(sort)) {
            exercises.sort(Comparator.comparing(ExerciseCatalog::getName).reversed());
        } else if ("name".equals(sort)) {
            exercises.sort(Comparator.comparing(ExerciseCatalog::getName));
        }

        return exercises.stream()
                .map(exercise -> ExerciseCatalogResponse.builder()
                        .id(exercise.getId())
                        .name(exercise.getName())
                        .imageUrl(exercise.getImageUrl())
                        .animationUrl(exercise.getAnimationUrl())
                        .thumbnail(exercise.getThumbnail())
                        .bodyRegion(exercise.getBodyRegion())
                        .equipment(exercise.getEquipment())
                        .primaryMuscle(exercise.getPrimaryMuscle())
                        .secondaryMuscles(exercise.getSecondaryMuscles())
                        .exerciseType(exercise.getExerciseType())
                        .difficulty(exercise.getDifficulty())
                        .movementPattern(exercise.getMovementPattern())
                        .tags(exercise.getTags())
                        .build())
                .toList();
    }

    public List<ExerciseCatalogResponse> searchExercises(String query) {

        return repository.findByNameContainingIgnoreCase(query)
                .stream()
                .map(exercise -> ExerciseCatalogResponse.builder()
                        .id(exercise.getId())
                        .name(exercise.getName())
                        .imageUrl(exercise.getImageUrl())
                        .animationUrl(exercise.getAnimationUrl())
                        .thumbnail(exercise.getThumbnail())
                        .bodyRegion(exercise.getBodyRegion())
                        .equipment(exercise.getEquipment())
                        .primaryMuscle(exercise.getPrimaryMuscle())
                        .secondaryMuscles(exercise.getSecondaryMuscles())
                        .exerciseType(exercise.getExerciseType())
                        .difficulty(exercise.getDifficulty())
                        .movementPattern(exercise.getMovementPattern())
                        .tags(exercise.getTags())
                        .build())
                .toList();
    }

    public ExerciseCatalogDetailsResponse getExercise(String id) {

        var exercise = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        return ExerciseCatalogDetailsResponse.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .imageUrl(exercise.getImageUrl())
                .animationUrl(exercise.getAnimationUrl())
                .thumbnail(exercise.getThumbnail())
                .bodyRegion(exercise.getBodyRegion())
                .equipment(exercise.getEquipment())
                .primaryMuscle(exercise.getPrimaryMuscle())
                .secondaryMuscles(exercise.getSecondaryMuscles())
                .exerciseType(exercise.getExerciseType())
                .difficulty(exercise.getDifficulty())
                .movementPattern(exercise.getMovementPattern())
                .tags(exercise.getTags())
                .build();
    }
}