package com.traintogain.backend.catalog.repository;

import com.traintogain.backend.catalog.model.ExerciseCatalog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExerciseCatalogRepository extends MongoRepository<ExerciseCatalog, String> {
}