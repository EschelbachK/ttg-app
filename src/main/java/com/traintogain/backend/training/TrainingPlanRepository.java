package com.traintogain.backend.training;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface TrainingPlanRepository
        extends MongoRepository<TrainingPlan, String> {

    List<TrainingPlan> findByUserIdAndArchivedFalse(String userId);

    List<TrainingPlan> findByUserIdAndArchivedTrue(String userId);

    boolean existsByUserId(String userId);

    Optional<TrainingPlan> findByIdAndUserId(String id, String userId);
}