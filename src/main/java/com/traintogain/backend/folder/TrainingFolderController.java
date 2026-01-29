package com.traintogain.backend.folder;

import com.traintogain.backend.common.BodyRegion;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-folders")
public class TrainingFolderController {

    private final TrainingFolderService folderService;

    public TrainingFolderController(TrainingFolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping
    public TrainingFolder createFolder(
            @RequestParam String trainingPlanId,
            @RequestParam String name,
            @RequestParam BodyRegion bodyRegion,
            @RequestParam int order
    ) {
        return folderService.createFolder(
                trainingPlanId,
                name,
                bodyRegion,
                order
        );
    }

    @GetMapping
    public List<TrainingFolder> getFolders(
            @RequestParam String trainingPlanId
    ) {
        return folderService.getFoldersForPlan(trainingPlanId);
    }
}
