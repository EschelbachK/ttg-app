package com.traintogain.backend.folder;

import com.traintogain.backend.common.BodyRegion;
import com.traintogain.backend.exercise.TrainingExerciseRepository;
import com.traintogain.backend.folder.dto.UpdateTrainingFolderRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingFolderService {

    private final TrainingFolderRepository trainingFolderRepository;
    private final TrainingExerciseRepository trainingExerciseRepository;

    public TrainingFolderService(
            TrainingFolderRepository trainingFolderRepository,
            TrainingExerciseRepository trainingExerciseRepository
    ) {
        this.trainingFolderRepository = trainingFolderRepository;
        this.trainingExerciseRepository = trainingExerciseRepository;
    }

    public TrainingFolder createFolder(
            String trainingPlanId,
            String name,
            BodyRegion bodyRegion,
            int order
    ) {
        TrainingFolder folder = new TrainingFolder(
                trainingPlanId,
                name,
                bodyRegion,
                order
        );

        return trainingFolderRepository.save(folder);
    }

    public List<TrainingFolder> getFoldersForPlan(String trainingPlanId) {
        return trainingFolderRepository
                .findByTrainingPlanIdOrderByOrderAsc(trainingPlanId);
    }

    public TrainingFolder updateFolder(String id, UpdateTrainingFolderRequest request) {
        TrainingFolder folder = trainingFolderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

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
                        .findByTrainingPlanIdOrderByOrderAsc(folder.getTrainingPlanId());

        for (TrainingFolder f : folders) {
            if (!f.getId().equals(folder.getId()) && f.getOrder() >= newOrder) {
                f.setOrder(f.getOrder() + 1);
            }
        }

        trainingFolderRepository.saveAll(folders);
    }

    public void deleteFolder(String id) {
        TrainingFolder folder = trainingFolderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        trainingExerciseRepository.deleteByFolderId(folder.getId());
        trainingFolderRepository.delete(folder);
    }
}
