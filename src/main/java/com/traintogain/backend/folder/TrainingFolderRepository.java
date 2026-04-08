package com.traintogain.backend.folder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TrainingFolderRepository
        extends MongoRepository<TrainingFolder, String> {

    List<TrainingFolder> findByUserIdAndArchived(String userId, boolean archived);

    List<TrainingFolder> findByUserIdAndTrainingPlanIdOrderByOrderAsc(
            String userId,
            String trainingPlanId
    );

    Page<TrainingFolder> findByUserIdAndTrainingPlanIdOrderByOrderAsc(
            String userId,
            String trainingPlanId,
            Pageable pageable
    );

    Page<TrainingFolder> findByUserIdAndTrainingPlanIdAndArchivedOrderByOrderAsc(
            String userId,
            String trainingPlanId,
            boolean archived,
            Pageable pageable
    );
}