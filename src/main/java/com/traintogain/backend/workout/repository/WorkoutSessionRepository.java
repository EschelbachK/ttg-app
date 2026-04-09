package com.traintogain.backend.workout.repository;

import com.traintogain.backend.workout.domain.WorkoutSession;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.List;

public interface WorkoutSessionRepository extends MongoRepository<WorkoutSession, String> {

    List<WorkoutSession> findByUserId(String userId);

    Optional<WorkoutSession> findFirstByUserIdAndFinishedAtIsNull(String userId);
}