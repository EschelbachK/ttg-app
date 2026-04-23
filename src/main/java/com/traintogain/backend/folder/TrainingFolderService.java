package com.traintogain.backend.folder;

import com.traintogain.backend.exception.ForbiddenException;
import com.traintogain.backend.exception.NotFoundException;
import com.traintogain.backend.exercise.TrainingExercise;
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
            Integer order
    ) {
        TrainingPlan plan = trainingPlanRepository
                .findByIdAndUserId(trainingPlanId, userId)
                .orElseThrow(() -> new NotFoundException("Trainingsplan wurde nicht gefunden"));

        List<TrainingFolder> folders =
                trainingFolderRepository.findByUserIdAndTrainingPlanIdOrderByOrderAsc(userId, plan.getId());

        int newOrder = (order != null && order >= 0 && order <= folders.size())
                ? order
                : folders.size();

        TrainingFolder folder = new TrainingFolder(
                plan.getId(),
                name,
                newOrder
        );

        folder.setUserId(plan.getUserId());

        folders.add(newOrder, folder);
        reassignOrder(folders);

        trainingFolderRepository.saveAll(folders);

        return folder;
    }

    public Page<TrainingFolder> getFoldersForPlan(
            String userId,
            String planId,
            Boolean archived,
            Pageable pageable
    ) {
        TrainingPlan plan = trainingPlanRepository
                .findByIdAndUserId(planId, userId)
                .orElseThrow(() -> new NotFoundException("Trainingsplan wurde nicht gefunden"));

        if (archived == null) {
            return trainingFolderRepository
                    .findByUserIdAndTrainingPlanIdOrderByOrderAsc(
                            userId,
                            plan.getId(),
                            pageable
                    );
        }

        return trainingFolderRepository
                .findByUserIdAndTrainingPlanIdAndArchivedOrderByOrderAsc(
                        userId,
                        plan.getId(),
                        archived,
                        pageable
                );
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

        if (request.getOrder() != null && request.getOrder() >= 0) {
            moveFolder(folder, request.getOrder());
        }

        return trainingFolderRepository.save(folder);
    }

    private void moveFolder(TrainingFolder target, int newOrder) {
        List<TrainingFolder> folders =
                new java.util.ArrayList<>(
                        trainingFolderRepository.findByUserIdAndTrainingPlanIdOrderByOrderAsc(
                                target.getUserId(),
                                target.getTrainingPlanId()
                        )
                );

        folders.removeIf(f -> f.getId().equals(target.getId()));

        if (newOrder > folders.size()) {
            newOrder = folders.size();
        }

        folders.add(newOrder, target);
        reassignOrder(folders);

        trainingFolderRepository.saveAll(folders);
    }

    public void deleteFolder(String id, String userId) {
        TrainingFolder folder = trainingFolderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Muskelgruppe wurde nicht gefunden"));

        if (!folder.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff auf diese Muskelgruppe");
        }

        List<TrainingFolder> folders =
                new java.util.ArrayList<>(
                        trainingFolderRepository.findByUserIdAndTrainingPlanIdOrderByOrderAsc(
                                folder.getUserId(),
                                folder.getTrainingPlanId()
                        )
                );

        trainingExerciseRepository.deleteByFolderId(folder.getId());
        folders.removeIf(f -> f.getId().equals(folder.getId()));

        reassignOrder(folders);
        trainingFolderRepository.saveAll(folders);

        trainingFolderRepository.delete(folder);
    }

    public void archiveFolder(String id, String userId) {
        TrainingFolder folder = trainingFolderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Muskelgruppe wurde nicht gefunden"));

        if (!folder.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff auf diese Muskelgruppe");
        }

        folder.setArchived(true);
        trainingFolderRepository.save(folder);
    }

    public void duplicateFolder(String id, String userId) {
        TrainingFolder original = trainingFolderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Muskelgruppe wurde nicht gefunden"));

        if (!original.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff auf diese Muskelgruppe");
        }

        List<TrainingFolder> folders =
                trainingFolderRepository.findByUserIdAndTrainingPlanIdOrderByOrderAsc(
                        original.getUserId(),
                        original.getTrainingPlanId()
                );

        TrainingFolder copy = new TrainingFolder(
                original.getTrainingPlanId(),
                original.getName(),
                original.getOrder() + 1
        );

        copy.setUserId(original.getUserId());

        int insertIndex = Math.min(original.getOrder() + 1, folders.size());
        folders.add(insertIndex, copy);

        reassignOrder(folders);
        trainingFolderRepository.saveAll(folders);

        List<TrainingExercise> exercises =
                trainingExerciseRepository.findByFolderId(original.getId());

        for (TrainingExercise ex : exercises) {
            TrainingExercise copyEx = ex.copyForFolder(copy.getId());
            trainingExerciseRepository.save(copyEx);
        }
    }

    private void reassignOrder(List<TrainingFolder> folders) {
        for (int i = 0; i < folders.size(); i++) {
            folders.get(i).setOrder(i);
        }
    }

    public List<TrainingFolder> getArchivedFolders(String userId) {
        return trainingFolderRepository.findByUserIdAndArchived(userId, true);
    }

    public void restoreFolder(String id, String userId) {
        TrainingFolder folder = trainingFolderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Muskelgruppe wurde nicht gefunden"));

        if (!folder.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff");
        }

        folder.setArchived(false);
        trainingFolderRepository.save(folder);
    }

    public void updateOrder(String id, String userId, int newOrder) {
        TrainingFolder folder = trainingFolderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nicht gefunden"));

        if (!folder.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff");
        }

        moveFolder(folder, newOrder);
    }

    public String getPlanName(String planId) {
        return trainingPlanRepository.findById(planId)
                .map(TrainingPlan::getTitle)
                .orElse("Unbekannt");
    }

    public boolean isPlanArchived(String planId) {
        return trainingPlanRepository.findById(planId)
                .map(TrainingPlan::isArchived)
                .orElse(false);
    }
}