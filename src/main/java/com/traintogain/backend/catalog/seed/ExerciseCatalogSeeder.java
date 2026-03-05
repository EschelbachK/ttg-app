package com.traintogain.backend.catalog.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traintogain.backend.catalog.model.ExerciseCatalog;
import com.traintogain.backend.catalog.repository.ExerciseCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExerciseCatalogSeeder implements CommandLineRunner {

    private final ExerciseCatalogRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {

        if (repository.count() > 0) {
            return;
        }

        ClassPathResource resource = new ClassPathResource("catalog/exercises.json");

        try (InputStream input = resource.getInputStream()) {

            List<ExerciseCatalog> exercises =
                    objectMapper.readValue(input, new TypeReference<>() {});

            repository.saveAll(exercises);

            System.out.println("Seeded " + exercises.size() + " exercises.");
        }
    }
}