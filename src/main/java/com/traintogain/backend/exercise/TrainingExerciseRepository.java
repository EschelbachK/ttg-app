package com.traintogain.backend.exercise;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TrainingExerciseRepository
        extends MongoRepository<TrainingExercise, String> {

    Page<TrainingExercise> findByUserIdAndFolderId(
            String userId,
            String folderId,
            Pageable pageable
    );

    List<TrainingExercise> findByFolderId(String folderId); // 🔥 HINZUFÜGEN

    void deleteByFolderId(String folderId); // 🔥 HINZUFÜGEN
}