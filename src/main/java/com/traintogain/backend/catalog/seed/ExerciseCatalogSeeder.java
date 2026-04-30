package com.traintogain.backend.catalog.seed;

import com.traintogain.backend.catalog.model.ExerciseCatalog;
import com.traintogain.backend.catalog.repository.ExerciseCatalogRepository;
import com.traintogain.backend.catalog.validation.ExerciseValidator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ExerciseCatalogSeeder {

    private final ExerciseCatalogRepository repository;
    private final ObjectMapper objectMapper;

    @EventListener(ApplicationReadyEvent.class)
    public void seed() {
        if (repository.count() > 0) return;

        List<ExerciseCatalog> exercises = load();

        if (exercises.isEmpty()) {
            throw new IllegalStateException("exercise json empty");
        }

        validateUniqueIds(exercises);
        validateExercises(exercises);
        applyDefaults(exercises);

        repository.saveAll(exercises);
    }

    private List<ExerciseCatalog> load() {
        try (InputStream is = new ClassPathResource("exercises.json").getInputStream()) {
            return Optional.ofNullable(
                    objectMapper.readValue(is, new TypeReference<List<ExerciseCatalog>>() {})
            ).orElse(List.of());
        } catch (Exception e) {
            throw new RuntimeException("failed to load exercise json", e);
        }
    }

    private void validateUniqueIds(List<ExerciseCatalog> exercises) {
        Set<String> ids = new HashSet<>();

        for (ExerciseCatalog e : exercises) {
            String id = e.getId();

            if (id == null || id.isBlank()) {
                throw new IllegalStateException("exercise with missing id");
            }

            if (!ids.add(id)) {
                throw new IllegalStateException("duplicate exercise id: " + id);
            }
        }
    }

    private void validateExercises(List<ExerciseCatalog> exercises) {
        for (ExerciseCatalog e : exercises) {
            List<String> errors = ExerciseValidator.validate(e);

            if (!errors.isEmpty()) {
                throw new IllegalStateException(
                        "invalid exercise: " + e.getId() + " -> " + errors
                );
            }
        }
    }

    private void applyDefaults(List<ExerciseCatalog> exercises) {
        for (ExerciseCatalog e : exercises) {
            if (e.getEquipment() == null) e.setEquipment(new ArrayList<>());
            if (e.getSecondaryMuscles() == null) e.setSecondaryMuscles(new ArrayList<>());
            if (e.getStabilizers() == null) e.setStabilizers(new ArrayList<>());
            if (e.getTags() == null) e.setTags(new ArrayList<>());
        }
    }
}