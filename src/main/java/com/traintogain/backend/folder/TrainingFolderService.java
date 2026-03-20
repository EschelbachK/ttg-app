package com.traintogain.backend.folder;

import com.traintogain.backend.common.BodyRegion;
import com.traintogain.backend.exception.ForbiddenException;
import com.traintogain.backend.exception.NotFoundException;
import com.traintogain.backend.exercise.TrainingExerciseRepository;
import com.traintogain.backend.folder.dto.UpdateTrainingFolderRequest;
import com.traintogain.backend.training.TrainingPlan;
import com.traintogain.backend.training.TrainingPlanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingFolderService {

    private final TrainingFolderRepository trainingFolderRepository;
    private final TrainingExerciseRepository trainingExerciseRepository;
    private final TrainingPlanRepository trainingPlanRepository;

    public TrainingFolderService(
            TrainingFolderRepository trainingFolderRepository,
            TrainingExerciseRepository trainingExerciseRepository,
            TrainingPlanRepository trainingPlanRepository
    ) {
        this.trainingFolderRepository = trainingFolderRepository;
        this.trainingExerciseRepository = trainingExerciseRepository;
        this.trainingPlanRepository = trainingPlanRepository;
    }

    public TrainingFolder createFolder(
            String userId,
            String trainingPlanId,
            String name,
            BodyRegion bodyRegion,
            int order
    ) {
        TrainingPlan plan = trainingPlanRepository
                .findByIdAndUserId(trainingPlanId, userId)
                .orElseThrow(() -> new NotFoundException("Trainingsplan wurde nicht gefunden"));

        int nextOrder = trainingFolderRepository
                .findByUserIdAndTrainingPlanIdOrderByOrderAsc(userId, trainingPlanId)
                .size();

        TrainingFolder folder = new TrainingFolder(
                plan.getId(),
                name,
                bodyRegion,
                nextOrder
        );

        folder.setUserId(userId);

        return trainingFolderRepository.save(folder);
    }

    public Page<TrainingFolder> getFoldersForPlan(
            String userId,
            String planId,
            Pageable pageable
    ) {
        TrainingPlan plan = trainingPlanRepository
                .findByIdAndUserId(planId, userId)
                .orElseThrow(() -> new NotFoundException("Trainingsplan wurde nicht gefunden"));

        return trainingFolderRepository.findByUserIdAndTrainingPlanId(userId, plan.getId(), pageable);
    }

    public TrainingFolder updateFolder(String id, String userId, UpdateTrainingFolderRequest request) {
        TrainingFolder folder = trainingFolderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Muskelgruppe wurde nicht gefunden"));

        if (!folder.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff auf diese Muskelgruppe");
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            folder.setName(request.getName());
        }

        if (request.getBodyRegion() != null) {
            folder.setBodyRegion(request.getBodyRegion());
        }

        if (request.getOrder() != null
                && request.getOrder() >= 0
                && request.getOrder() != folder.getOrder()) {

            reorderFolders(folder, request.getOrder());
        }

        return trainingFolderRepository.save(folder);
    }

    private void reorderFolders(TrainingFolder target, int newOrder) {
        List<TrainingFolder> folders =
                trainingFolderRepository.findByUserIdAndTrainingPlanIdOrderByOrderAsc(
                        target.getUserId(),
                        target.getTrainingPlanId()
                );

        folders.removeIf(f -> f.getId().equals(target.getId()));

        if (newOrder > folders.size()) {
            newOrder = folders.size();
        }

        folders.add(newOrder, target);

        for (int i = 0; i < folders.size(); i++) {
            folders.get(i).setOrder(i);
        }

        trainingFolderRepository.saveAll(folders);
    }

    public void deleteFolder(String id, String userId) {
        TrainingFolder folder = trainingFolderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Muskelgruppe wurde nicht gefunden"));

        if (!folder.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff auf diese Muskelgruppe");
        }

        trainingExerciseRepository.deleteByFolderId(folder.getId());
        trainingFolderRepository.delete(folder);
    }
}