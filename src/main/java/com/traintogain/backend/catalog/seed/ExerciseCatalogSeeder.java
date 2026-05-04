package com.traintogain.backend.catalog.seed;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traintogain.backend.catalog.model.ExerciseCatalog;
import com.traintogain.backend.catalog.model.Muscle;
import com.traintogain.backend.catalog.repository.ExerciseCatalogRepository;
import com.traintogain.backend.catalog.validation.ExerciseValidator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;

@Component
public class ExerciseCatalogSeeder implements CommandLineRunner {

    private final ExerciseCatalogRepository repository;
    private final ObjectMapper mapper;
    private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public ExerciseCatalogSeeder(
            ExerciseCatalogRepository repository,
            ObjectMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seed();
    }

    public void seed() {
        try {
            Resource[] resources = resolver.getResources("classpath:exercise_catalog/*.json");

            Map<String, ExerciseCatalog> exerciseMap = new HashMap<>();

            for (Resource resource : resources) {

                try (InputStream is = resource.getInputStream()) {

                    JsonNode root = mapper.readTree(is);
                    JsonNode exercisesNode = root.get("exercises");

                    if (exercisesNode == null || !exercisesNode.isArray()) {
                        throw new IllegalStateException("invalid exercises array in " + resource.getFilename());
                    }

                    for (JsonNode node : exercisesNode) {

                        ExerciseCatalog exercise = safeMap(node);

                        List<String> errors = ExerciseValidator.validate(exercise);
                        if (!errors.isEmpty()) {
                            throw new IllegalStateException(
                                    "validation failed for " + exercise.getId() + ": " + errors
                            );
                        }

                        if (exerciseMap.containsKey(exercise.getId())) {
                            throw new IllegalStateException("duplicate id: " + exercise.getId());
                        }

                        exerciseMap.put(exercise.getId(), exercise);
                    }
                }
            }

            List<ExerciseCatalog> existing = repository.findAll();
            Map<String, ExerciseCatalog> existingMap = new HashMap<>();

            for (ExerciseCatalog e : existing) {
                existingMap.put(e.getId(), e);
            }

            List<ExerciseCatalog> toSave = new ArrayList<>();

            for (ExerciseCatalog incoming : exerciseMap.values()) {

                ExerciseCatalog old = existingMap.get(incoming.getId());

                if (old != null) {
                    toSave.add(merge(old, incoming));
                } else {
                    toSave.add(incoming);
                }
            }

            repository.saveAll(toSave);

        } catch (Exception e) {
            throw new RuntimeException("seeder failed", e);
        }
    }

    private ExerciseCatalog safeMap(JsonNode node) {

        ExerciseCatalog exercise = mapper.convertValue(node, ExerciseCatalog.class);

        if (exercise.getPrimaryMuscle() == null && node.has("primaryMuscle")) {
            exercise.setPrimaryMuscle(resolveMuscle(node.get("primaryMuscle").asText()));
        }

        if (node.has("secondaryMuscles")) {
            List<Muscle> list = new ArrayList<>();

            for (JsonNode m : node.get("secondaryMuscles")) {
                Muscle resolved = resolveMuscle(m.asText());
                if (resolved != null) list.add(resolved);
            }

            exercise.setSecondaryMuscles(list);
        }

        return exercise;
    }

    private Muscle resolveMuscle(String raw) {

        if (raw == null) return null;

        try {
            return Muscle.valueOf(raw);
        } catch (Exception ignored) {}

        for (Muscle m : Muscle.values()) {
            if (m.name().equalsIgnoreCase(raw)) {
                return m;
            }
        }

        throw new IllegalStateException("invalid muscle: " + raw);
    }

    private ExerciseCatalog merge(ExerciseCatalog existing, ExerciseCatalog incoming) {

        existing.setName(incoming.getName());
        existing.setBodyRegion(incoming.getBodyRegion());
        existing.setFamily(incoming.getFamily());
        existing.setMovementPattern(incoming.getMovementPattern());
        existing.setBasePattern(incoming.getBasePattern());
        existing.setMovementPlane(incoming.getMovementPlane());
        existing.setMechanic(incoming.getMechanic());
        existing.setLoadType(incoming.getLoadType());
        existing.setLaterality(incoming.getLaterality());
        existing.setPrimaryMuscle(incoming.getPrimaryMuscle());
        existing.setSecondaryMuscles(incoming.getSecondaryMuscles());
        existing.setStabilizers(incoming.getStabilizers());
        existing.setEquipment(incoming.getEquipment());
        existing.setExerciseType(incoming.getExerciseType());
        existing.setDifficulty(incoming.getDifficulty());
        existing.setTags(incoming.getTags());
        existing.setMedia(incoming.getMedia());
        existing.setExecution(incoming.getExecution());
        existing.setProgression(incoming.getProgression());
        existing.setSafety(incoming.getSafety());
        existing.setInstructions(incoming.getInstructions());
        existing.setTips(incoming.getTips());
        existing.setCommonMistakes(incoming.getCommonMistakes());

        return existing;
    }
}