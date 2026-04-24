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
                    objectMapper.readValue(input, new TypeReference<>() {});

            exercises.forEach(e -> {

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
                    e.setMovementPattern(detectPattern(e));
                }

                if (e.getTags() == null || e.getTags().isEmpty()) {
                    e.setTags(generateTags(e));
                }

                if (e.getThumbnail() == null) {
                    e.setThumbnail(generateThumbnail(e));
                }

                if (e.getImageUrl() == null) {
                    e.setImageUrl(e.getThumbnail());
                }

                if (e.getAnimationUrl() == null) {
                    e.setAnimationUrl(null);
                }
            });

            repository.saveAll(exercises);
        }
    }

    private MovementPattern detectPattern(ExerciseCatalog e) {

        String name = e.getName().toLowerCase();

        if (name.contains("press") || name.contains("drücken") || name.contains("push")) {
            return MovementPattern.PUSH;
        }

        if (name.contains("row") || name.contains("rudern") || name.contains("pull") || name.contains("klimmzug")) {
            return MovementPattern.PULL;
        }

        if (name.contains("squat") || name.contains("kniebeuge")) {
            return MovementPattern.SQUAT;
        }

        if (name.contains("deadlift") || name.contains("kreuzheben")) {
            return MovementPattern.HINGE;
        }

        if (name.contains("lunge") || name.contains("ausfallschritt")) {
            return MovementPattern.LUNGE;
        }

        if (e.getBodyRegion() == BodyRegion.BAUCH) {
            return MovementPattern.CORE;
        }

        return MovementPattern.FULL_BODY;
    }

    private List<String> generateTags(ExerciseCatalog e) {

        List<String> tags = new ArrayList<>();

        tags.add(e.getDifficulty().name().toLowerCase());
        tags.add(e.getExerciseType().name().toLowerCase());
        tags.add(e.getBodyRegion().name().toLowerCase());

        if (e.getEquipment() != null) {
            tags.add(e.getEquipment().name().toLowerCase());
        }

        return tags;
    }

    private String generateThumbnail(ExerciseCatalog e) {
        return "/images/exercises/" + e.getId() + ".png";
    }
}