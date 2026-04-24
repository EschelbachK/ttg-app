package com.traintogain.backend.catalog.service;

import com.traintogain.backend.catalog.dto.ExerciseCatalogDetailsResponse;
import com.traintogain.backend.catalog.dto.ExerciseCatalogResponse;
import com.traintogain.backend.catalog.model.*;
import com.traintogain.backend.catalog.repository.ExerciseCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

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

        var pageable = PageRequest.of(page, size);

        Page<ExerciseCatalog> result;

        if (bodyRegion != null && equipment != null && pattern != null) {
            result = repository.findByBodyRegionAndEquipmentAndMovementPattern(bodyRegion, equipment, pattern, pageable);
        } else if (bodyRegion != null && equipment != null) {
            result = repository.findByBodyRegionAndEquipment(bodyRegion, equipment, pageable);
        } else if (bodyRegion != null && pattern != null) {
            result = repository.findByBodyRegionAndMovementPattern(bodyRegion, pattern, pageable);
        } else if (equipment != null && pattern != null) {
            result = repository.findByEquipmentAndMovementPattern(equipment, pattern, pageable);
        } else if (bodyRegion != null) {
            result = repository.findByBodyRegion(bodyRegion, pageable);
        } else if (equipment != null) {
            result = repository.findByEquipment(equipment, pageable);
        } else if (pattern != null) {
            result = repository.findByMovementPattern(pattern, pageable);
        } else {
            result = repository.findAll(pageable);
        }

        List<ExerciseCatalog> exercises = new ArrayList<>(result.getContent());

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

        return exercises.stream().map(this::mapToResponse).toList();
    }

    public List<ExerciseCatalogResponse> searchExercises(String query) {

        String q = query.toLowerCase();

        return repository.findAll().stream()
                .map(e -> Map.entry(e, score(e, q)))
                .filter(entry -> entry.getValue() > 0)
                .sorted(Map.Entry.<ExerciseCatalog, Integer>comparingByValue().reversed())
                .map(entry -> mapToResponse(entry.getKey()))
                .toList();
    }

    private int score(ExerciseCatalog e, String q) {

        int score = 0;

        if (e.getName().toLowerCase().contains(q)) score += 100;

        if (e.getPrimaryMuscle().name().toLowerCase().contains(q)) score += 50;

        if (e.getTags() != null) {
            for (String tag : e.getTags()) {
                if (tag.toLowerCase().contains(q)) score += 30;
            }
        }

        if (e.getSecondaryMuscles() != null) {
            for (Muscle m : e.getSecondaryMuscles()) {
                if (m.name().toLowerCase().contains(q)) score += 20;
            }
        }

        return score;
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

    private ExerciseCatalogResponse mapToResponse(ExerciseCatalog e) {
        return ExerciseCatalogResponse.builder()
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