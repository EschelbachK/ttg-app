package com.traintogain.backend.training;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface TrainingPlanRepository
        extends MongoRepository<TrainingPlan, String> {

    // 🔥 WICHTIG: MIT ORDER SORTIERUNG
    List<TrainingPlan> findByUserIdAndArchivedFalseOrderByOrderAsc(String userId);

    List<TrainingPlan> findByUserIdAndArchivedTrueOrderByOrderAsc(String userId);

    boolean existsByUserId(String userId);

    Optional<TrainingPlan> findByIdAndUserId(String id, String userId);
}