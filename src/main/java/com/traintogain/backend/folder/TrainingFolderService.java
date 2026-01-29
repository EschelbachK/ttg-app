package com.traintogain.backend.folder;

import com.traintogain.backend.common.BodyRegion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingFolderService {

    private final TrainingFolderRepository folderRepository;

    public TrainingFolderService(TrainingFolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public TrainingFolder createFolder(
            String trainingPlanId,
            String name,
            BodyRegion bodyRegion,
            int order
    ) {
        TrainingFolder folder =
                new TrainingFolder(trainingPlanId, name, bodyRegion, order);

        return folderRepository.save(folder);
    }

    public List<TrainingFolder> getFoldersForPlan(String trainingPlanId) {
        return folderRepository
                .findByTrainingPlanIdOrderByOrderAsc(trainingPlanId);
    }
}
