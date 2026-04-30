package com.traintogain.backend.catalog.repository;

import com.traintogain.backend.catalog.model.ExerciseCatalog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExerciseCatalogRepository extends MongoRepository<ExerciseCatalog, String> {

    List<ExerciseCatalog> findByNameContainingIgnoreCase(String name);
}