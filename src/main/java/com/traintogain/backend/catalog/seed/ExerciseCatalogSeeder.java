package com.traintogain.backend.catalog.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traintogain.backend.catalog.model.*;
import com.traintogain.backend.catalog.repository.ExerciseCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExerciseCatalogSeeder implements CommandLineRunner {

    private final ExerciseCatalogRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {

        if (repository.count() > 0) return;

        ClassPathResource resource = new ClassPathResource("catalog/exercises.json");

        try (InputStream input = resource.getInputStream()) {

            List<ExerciseCatalog> exercises =
                    objectMapper.readValue(input, new TypeReference<List<ExerciseCatalog>>() {});

            exercises.forEach(this::enrich);

            repository.saveAll(exercises);
        }
    }

    private void enrich(ExerciseCatalog e) {

        if (e.getPrimaryMuscle() == null) {
            e.setPrimaryMuscle(Muscle.BRUST);
        }

        if (e.getExerciseType() == null) {
            e.setExerciseType(ExerciseType.GRUNDUEBUNG);
        }

        if (e.getDifficulty() == null) {
            e.setDifficulty(Difficulty.MITTEL);
        }

        if (e.getMovementPattern() == null) {
            e.setMovementPattern(mapPattern(e));
        }

        if (e.getTags() == null || e.getTags().isEmpty()) {
            e.setTags(generateTags(e));
        }

        if (e.getImageUrl() == null) {
            e.setImageUrl("/images/" + e.getId() + ".png");
        }

        if (e.getAnimationUrl() == null) {
            e.setAnimationUrl("/animations/" + e.getId() + ".gif");
        }

        if (e.getThumbnail() == null) {
            e.setThumbnail("/thumbnails/" + e.getId() + ".jpg");
        }
    }

    private MovementPattern mapPattern(ExerciseCatalog e) {
        return switch (e.getBodyRegion()) {
            case BRUST, SCHULTERN, TRIZEPS -> MovementPattern.PUSH;
            case RUECKEN, BIZEPS -> MovementPattern.PULL;
            case BEINE -> MovementPattern.SQUAT;
            case BAUCH -> MovementPattern.CORE;
            case GANZKOERPER, CARDIO -> MovementPattern.FULL_BODY;
            default -> MovementPattern.FULL_BODY;
        };
    }

    private List<String> generateTags(ExerciseCatalog e) {

        List<String> tags = new ArrayList<>();

        tags.add(e.getPrimaryMuscle().name().toLowerCase());
        tags.add(e.getEquipment().name().toLowerCase());
        tags.add(e.getExerciseType().name().toLowerCase());
        tags.add(e.getMovementPattern().name().toLowerCase());

        return tags;
    }
}