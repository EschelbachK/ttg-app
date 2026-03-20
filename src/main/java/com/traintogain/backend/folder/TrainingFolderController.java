package com.traintogain.backend.folder;

import com.traintogain.backend.folder.dto.CreateTrainingFolderRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/training-plans/{planId}/folders")
public class TrainingFolderController {

    private final TrainingFolderService folderService;

    public TrainingFolderController(TrainingFolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping
    public ResponseEntity<TrainingFolder> createFolder(
            @PathVariable String planId,
            @Valid @RequestBody CreateTrainingFolderRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                folderService.createFolder(
                        authentication.getName(),
                        planId,
                        request.name(),
                        null,
                        request.order()
                )
        );
    }

    @GetMapping
    public Page<TrainingFolder> getFolders(
            @PathVariable String planId,
            @PageableDefault(sort = "order", direction = Sort.Direction.ASC) Pageable pageable,
            Authentication authentication
    ) {
        return folderService.getFoldersForPlan(
                authentication.getName(),
                planId,
                pageable
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFolder(
            @PathVariable String id,
            Authentication authentication
    ) {
        folderService.deleteFolder(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}