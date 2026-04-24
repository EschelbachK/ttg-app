package com.traintogain.backend.catalog.seeder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traintogain.backend.catalog.model.ExerciseCatalog;
import com.traintogain.backend.catalog.repository.ExerciseCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExerciseCatalogSeeder {

    private final ExerciseCatalogRepository repository;
    private final ObjectMapper objectMapper;

    @EventListener(ApplicationReadyEvent.class)
    public void seed() {

        try (InputStream inputStream = new ClassPathResource("exercise-catalog.json").getInputStream()) {

            List<ExerciseCatalog> incoming = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<ExerciseCatalog>>() {}
            );

            Map<String, ExerciseCatalog> existingMap = repository.findAll()
                    .stream()
                    .collect(Collectors.toMap(ExerciseCatalog::getId, e -> e));

            List<ExerciseCatalog> toSave = incoming.stream()
                    .map(e -> {

                        ExerciseCatalog existing = existingMap.get(e.getId());

                        if (existing == null) {
                            return e;
                        }

                        existing.setName(e.getName());
                        existing.setBodyRegion(e.getBodyRegion());
                        existing.setEquipment(e.getEquipment());
                        existing.setPrimaryMuscle(e.getPrimaryMuscle());
                        existing.setSecondaryMuscles(e.getSecondaryMuscles());
                        existing.setExerciseType(e.getExerciseType());
                        existing.setDifficulty(e.getDifficulty());
                        existing.setMovementPattern(e.getMovementPattern());
                        existing.setTags(e.getTags());
                        existing.setThumbnail(e.getThumbnail());
                        existing.setImageUrl(e.getImageUrl());
                        existing.setAnimationUrl(e.getAnimationUrl());
                        existing.setInstructions(e.getInstructions());
                        existing.setTips(e.getTips());
                        existing.setCommonMistakes(e.getCommonMistakes());

                        return existing;
                    })
                    .toList();

            repository.saveAll(toSave);

        } catch (Exception e) {
            throw new RuntimeException("failed to seed exercise catalog", e);
        }
    }
}