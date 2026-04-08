package com.traintogain.backend.training;

import com.traintogain.backend.exception.ForbiddenException;
import com.traintogain.backend.exception.NotFoundException;
import com.traintogain.backend.folder.TrainingFolder;
import com.traintogain.backend.folder.TrainingFolderRepository;
import com.traintogain.backend.exercise.TrainingExerciseRepository;
import com.traintogain.backend.training.dto.UpdateTrainingPlanRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final TrainingFolderRepository trainingFolderRepository;
    private final TrainingExerciseRepository trainingExerciseRepository;

    public TrainingPlanService(
            TrainingPlanRepository trainingPlanRepository,
            TrainingFolderRepository trainingFolderRepository,
            TrainingExerciseRepository trainingExerciseRepository
    ) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.trainingFolderRepository = trainingFolderRepository;
        this.trainingExerciseRepository = trainingExerciseRepository;
    }

    public TrainingPlan createPlan(String userId, String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title darf nicht leer sein");
        }

        TrainingPlan plan = new TrainingPlan(userId, title);
        return trainingPlanRepository.save(plan);
    }

    public List<TrainingPlan> getPlansForUser(String userId) {
        return trainingPlanRepository.findByUserIdAndArchivedFalse(userId);
    }

    public List<TrainingPlan> getArchivedPlansForUser(String userId) {
        return trainingPlanRepository.findByUserIdAndArchivedTrue(userId);
    }

    public TrainingPlan updatePlan(String id, String userId, UpdateTrainingPlanRequest request) {
        TrainingPlan plan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Trainingsplan wurde nicht gefunden"));

        if (!plan.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff auf diesen Trainingsplan");
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            plan.setTitle(request.getTitle());
        }

        if (request.getArchived() != null) {
            plan.setArchived(request.getArchived());
        }

        return trainingPlanRepository.save(plan);
    }

    public void archivePlan(String id, String userId) {
        TrainingPlan plan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Trainingsplan wurde nicht gefunden"));

        if (!plan.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff auf diesen Trainingsplan");
        }

        plan.setArchived(true);
        trainingPlanRepository.save(plan);
    }

    public void deletePlan(String id, String userId) {
        TrainingPlan plan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Trainingsplan wurde nicht gefunden"));

        if (!plan.getUserId().equals(userId)) {
            throw new ForbiddenException("Kein Zugriff auf diesen Trainingsplan");
        }

        List<TrainingFolder> folders = trainingFolderRepository
                .findByUserIdAndTrainingPlanIdOrderByOrderAsc(userId, plan.getId());

        for (TrainingFolder folder : folders) {
            trainingExerciseRepository.deleteByFolderId(folder.getId());
        }

        trainingFolderRepository.deleteAll(folders);
        trainingPlanRepository.delete(plan);
    }
}