package com.traintogain.backend.exercise;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TrainingExerciseRepository
        extends MongoRepository<TrainingExercise, String> {

    List<TrainingExercise> findByFolderId(String folderId);

    List<TrainingExercise> findByUserIdAndFolderId(String userId, String folderId);

    void deleteByFolderId(String folderId);
}