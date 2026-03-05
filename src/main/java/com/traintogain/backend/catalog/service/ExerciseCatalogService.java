package com.traintogain.backend.catalog.service;

import com.traintogain.backend.catalog.model.BodyRegion;
import com.traintogain.backend.catalog.model.ExerciseCatalog;
import com.traintogain.backend.catalog.repository.ExerciseCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseCatalogService {

    private final ExerciseCatalogRepository repository;

    public List<BodyRegion> getBodyRegions() {
        return Arrays.asList(BodyRegion.values());
    }

    public List<ExerciseCatalog> getExercisesByRegion(BodyRegion bodyRegion) {
        return repository.findByBodyRegion(bodyRegion);
    }

    public List<ExerciseCatalog> searchExercises(String query) {
        return repository.findByNameContainingIgnoreCase(query);
    }
}