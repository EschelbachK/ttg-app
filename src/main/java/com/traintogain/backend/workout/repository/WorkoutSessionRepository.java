package com.traintogain.backend.workout.repository;

import com.traintogain.backend.workout.domain.WorkoutSession;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface WorkoutSessionRepository extends MongoRepository<WorkoutSession, String> {

    List<WorkoutSession> findByUserId(String userId);

    Optional<WorkoutSession> findFirstByUserIdAndFinishedAtIsNull(String userId);
}