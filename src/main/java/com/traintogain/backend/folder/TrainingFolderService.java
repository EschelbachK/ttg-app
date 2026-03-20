package com.traintogain.backend.folder;

import com.traintogain.backend.common.BodyRegion;
import com.traintogain.backend.common.exception.ForbiddenException;
import com.traintogain.backend.common.exception.NotFoundException;
import com.traintogain.backend.exercise.TrainingExerciseRepository;
import com.traintogain.backend.folder.dto.UpdateTrainingFolderRequest;
import com.traintogain.backend.training.TrainingPlan;
import com.traintogain.backend.training.TrainingPlanRepository;
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
                .findById(trainingPlanId)
                .orElseThrow(() -> new NotFoundException("Trainingsplan wurde nicht gefunden"));

        if (!plan.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff auf diesen Trainingsplan");
        }

        TrainingFolder folder = new TrainingFolder(
                trainingPlanId,
                name,
                bodyRegion,
                order
        );

        folder.setUserId(userId);

        return trainingFolderRepository.save(folder);
    }

    public TrainingFolder updateFolder(String id, String userId, UpdateTrainingFolderRequest request) {
        TrainingFolder folder = trainingFolderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ordner wurde nicht gefunden"));

        if (!folder.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff auf diesen Ordner");
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
            folder.setOrder(request.getOrder());
        }

        return trainingFolderRepository.save(folder);
    }

    private void reorderFolders(TrainingFolder folder, int newOrder) {
        List<TrainingFolder> folders =
                trainingFolderRepository
                        .findByUserIdAndTrainingPlanIdOrderByOrderAsc(
                                folder.getUserId(),
                                folder.getTrainingPlanId()
                        );

        for (TrainingFolder f : folders) {
            if (!f.getId().equals(folder.getId()) && f.getOrder() >= newOrder) {
                f.setOrder(f.getOrder() + 1);
            }
        }

        trainingFolderRepository.saveAll(folders);
    }

    public void deleteFolder(String id, String userId) {
        TrainingFolder folder = trainingFolderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ordner wurde nicht gefunden"));

        if (!folder.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff auf diesen Ordner");
        }

        trainingExerciseRepository.deleteByFolderId(folder.getId());
        trainingFolderRepository.delete(folder);
    }
}