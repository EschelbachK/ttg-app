package com.traintogain.backend.catalog.seed;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traintogain.backend.catalog.model.ExerciseCatalog;
import com.traintogain.backend.catalog.model.Muscle;
import com.traintogain.backend.catalog.repository.ExerciseCatalogRepository;
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
            System.out.println("starting exercise catalog seeder");

            Resource[] resources = resolver.getResources("classpath:exercise_catalog/*.json");

            Map<String, ExerciseCatalog> exerciseMap = new HashMap<>();

            int success = 0;
            int failed = 0;

            for (Resource resource : resources) {

                System.out.println("processing file: " + resource.getFilename());

                try (InputStream is = resource.getInputStream()) {

                    JsonNode root = mapper.readTree(is);
                    JsonNode exercisesNode = root.get("exercises");

                    if (exercisesNode == null || !exercisesNode.isArray()) {
                        System.out.println("no exercises array: " + resource.getFilename());
                        continue;
                    }

                    for (JsonNode node : exercisesNode) {

                        try {
                            ExerciseCatalog exercise = safeMap(node);

                            if (!validate(exercise)) {
                                failed++;
                                continue;
                            }

                            if (exerciseMap.containsKey(exercise.getId())) {
                                System.out.println("duplicate id skipped: " + exercise.getId());
                                continue;
                            }

                            exerciseMap.put(exercise.getId(), exercise);
                            success++;

                        } catch (Exception ex) {
                            failed++;
                            System.out.println("❌ mapping failed: " + ex.getMessage());
                        }
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

            System.out.println("====== SEED SUMMARY ======");
            System.out.println("success: " + success);
            System.out.println("failed: " + failed);
            System.out.println("saved: " + toSave.size());

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

        System.out.println("⚠️ unknown muscle: " + raw);
        return null;
    }

    private boolean validate(ExerciseCatalog e) {

        if (e.getId() == null || e.getId().isBlank()) {
            return false;
        }

        if (e.getPrimaryMuscle() == null) {
            System.out.println("❌ invalid: missing primary muscle " + e.getId());
            return false;
        }

        if (e.getMovementPattern() == null ||
                e.getBodyRegion() == null ||
                e.getFamily() == null) {

            System.out.println("❌ invalid core fields " + e.getId());
            return false;
        }

        return true;
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