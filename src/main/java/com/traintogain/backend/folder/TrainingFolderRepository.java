package com.traintogain.backend.folder;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TrainingFolderRepository
        extends MongoRepository<TrainingFolder, String> {

    List<TrainingFolder> findByTrainingPlanIdOrderByOrderAsc(String trainingPlanId);

    List<TrainingFolder> findByUserIdAndTrainingPlanIdOrderByOrderAsc(String userId, String trainingPlanId);
}