package com.traintogain.backend.training;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TrainingPlanRepository
        extends MongoRepository<TrainingPlan, String> {

    List<TrainingPlan> findByUserId(String userId);

    List<TrainingPlan> findByUserIdAndArchivedFalse(String userId);

    List<TrainingPlan> findByUserIdAndArchivedTrue(String userId);
}