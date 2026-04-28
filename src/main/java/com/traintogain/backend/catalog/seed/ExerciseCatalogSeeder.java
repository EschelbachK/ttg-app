package com.traintogain.backend.catalog.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traintogain.backend.catalog.media.MediaAutoBuilder;
import com.traintogain.backend.catalog.model.ExerciseCatalog;
import com.traintogain.backend.catalog.repository.ExerciseCatalogRepository;
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
        if (exercises.isEmpty()) throw new IllegalStateException("exercise json empty");

        validateUniqueIds(exercises);
        ensureMovementBaseCompatibility(exercises);
        exercises.forEach(e -> {
            e.ensureSafeDefaults();
            if (e.getMedia() == null) e.setMedia(MediaAutoBuilder.build(e.getId()));
        });

        repository.saveAll(exercises);
    }

    private List<ExerciseCatalog> load() {
        try (InputStream is = new ClassPathResource("exercises.json").getInputStream()) {
            return Optional.ofNullable(objectMapper.readValue(is, new TypeReference<List<ExerciseCatalog>>() {}))
                    .orElse(List.of());
        } catch (Exception e) {
            throw new RuntimeException("failed to load exercise json", e);
        }
    }

    private void validateUniqueIds(List<ExerciseCatalog> exercises) {
        Set<String> ids = new HashSet<>();
        for (ExerciseCatalog e : exercises) {
            String id = e.getId();
            if (id == null || id.isBlank()) throw new IllegalStateException("exercise with missing id");
            if (!ids.add(id)) throw new IllegalStateException("duplicate exercise id: " + id);
        }
    }

    private void ensureMovementBaseCompatibility(List<ExerciseCatalog> exercises) {
        Set<String> allowedIsolations = Set.of(
                "ELBOW_FLEXION","ELBOW_EXTENSION","WRIST_FLEXION","WRIST_EXTENSION",
                "SCAPULAR_ELEVATION","LATERAL_RAISE","HORIZONTAL_ADDUCTION",
                "HORIZONTAL_ABDUCTION","ISOLATION_CARRY"
        );

        for (ExerciseCatalog e : exercises) {
            String movement = e.getMovementPattern().name();
            String base = e.getBasePattern().name();

            if (base == null || base.isBlank())
                throw new IllegalStateException("exercise " + e.getId() + " has missing basePattern");

            boolean valid = allowedIsolations.contains(base) || base.endsWith("_PATTERN") || movement.equals(base);
            if (!valid)
                throw new IllegalStateException(
                        "invalid exercise: " + e.getId() + " -> movementPattern=" + movement + ", basePattern=" + base
                );
        }
    }
}