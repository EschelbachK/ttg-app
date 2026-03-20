package com.traintogain.backend.folder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrainingFolderRepository
        extends MongoRepository<TrainingFolder, String> {

    Page<TrainingFolder> findByUserIdAndTrainingPlanId(
            String userId,
            String trainingPlanId,
            Pageable pageable
    );

    java.util.List<TrainingFolder> findByUserIdAndTrainingPlanIdOrderByOrderAsc(String userId, String trainingPlanId);
}