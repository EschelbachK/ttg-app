package com.traintogain.backend.exercise;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrainingExerciseRepository
        extends MongoRepository<TrainingExercise, String> {

    Page<TrainingExercise> findByUserIdAndFolderId(
            String userId,
            String folderId,
            Pageable pageable
    );
}