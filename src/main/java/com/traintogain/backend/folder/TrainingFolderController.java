package com.traintogain.backend.folder;

import com.traintogain.backend.folder.dto.CreateTrainingFolderRequest;
import com.traintogain.backend.folder.dto.UpdateTrainingFolderRequest;
import com.traintogain.backend.folder.dto.TrainingFolderResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TrainingFolderController {

    private final TrainingFolderService folderService;

    public TrainingFolderController(TrainingFolderService folderService) {
        this.folderService = folderService;
    }

    // CREATE
    @PostMapping("/training-plans/{planId}/folders")
    public ResponseEntity<TrainingFolderResponse> createFolder(
            @PathVariable String planId,
            @Valid @RequestBody CreateTrainingFolderRequest request,
            Authentication authentication
    ) {
        TrainingFolder folder = folderService.createFolder(
                authentication.getName(),
                planId,
                request.name(),
                request.order()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TrainingFolderResponse.fromEntity(
                        folder,
                        folderService.getPlanName(folder.getTrainingPlanId())
                ));
    }

    // GET BY PLAN
    @GetMapping("/training-plans/{planId}/folders")
    public Page<TrainingFolderResponse> getFolders(
            @PathVariable String planId,
            @RequestParam(required = false) Boolean archived,
            @PageableDefault(sort = "order", direction = Sort.Direction.ASC) Pageable pageable,
            Authentication authentication
    ) {
        return folderService.getFoldersForPlan(
                authentication.getName(),
                planId,
                archived, // 🔥 FIX: nicht mehr default false!
                pageable
        ).map(folder -> TrainingFolderResponse.fromEntity(
                folder,
                folderService.getPlanName(folder.getTrainingPlanId())
        ));
    }

    // 🔥 FINAL: NUR standalone archivierte Folder
    @GetMapping("/training-folders/archived")
    public List<TrainingFolderResponse> getArchivedFolders(Authentication authentication) {

        return folderService.getArchivedFolders(authentication.getName())
                .stream()

                /// 🔥 FILTER: nur Folder deren Plan NICHT archiviert ist
                .filter(folder ->
                        !folderService.isPlanArchived(folder.getTrainingPlanId())
                )

                .map(folder -> TrainingFolderResponse.fromEntity(
                        folder,
                        folderService.getPlanName(folder.getTrainingPlanId())
                ))
                .toList();
    }

    // UPDATE
    @PatchMapping("/training-folders/{id}")
    public ResponseEntity<TrainingFolderResponse> updateFolder(
            @PathVariable String id,
            @RequestBody UpdateTrainingFolderRequest request,
            Authentication authentication
    ) {
        TrainingFolder updated = folderService.updateFolder(
                id,
                authentication.getName(),
                request
        );

        return ResponseEntity.ok(
                TrainingFolderResponse.fromEntity(
                        updated,
                        folderService.getPlanName(updated.getTrainingPlanId())
                )
        );
    }

    // ORDER
    @PatchMapping("/training-folders/{id}/order")
    public ResponseEntity<Void> updateOrder(
            @PathVariable String id,
            @RequestParam int order,
            Authentication authentication
    ) {
        folderService.updateOrder(id, authentication.getName(), order);
        return ResponseEntity.ok().build();
    }

    // DELETE
    @DeleteMapping("/training-folders/{id}")
    public ResponseEntity<Void> deleteFolder(
            @PathVariable String id,
            Authentication authentication
    ) {
        folderService.deleteFolder(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    // ARCHIVE
    @PostMapping("/training-folders/{id}/archive")
    public ResponseEntity<Void> archiveFolder(
            @PathVariable String id,
            Authentication authentication
    ) {
        folderService.archiveFolder(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    // RESTORE
    @PatchMapping("/training-folders/{id}/restore")
    public ResponseEntity<Void> restoreFolder(
            @PathVariable String id,
            Authentication authentication
    ) {
        folderService.restoreFolder(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    // DUPLICATE
    @PostMapping("/training-folders/{id}/duplicate")
    public ResponseEntity<Void> duplicateFolder(
            @PathVariable String id,
            Authentication authentication
    ) {
        folderService.duplicateFolder(id, authentication.getName());
        return ResponseEntity.ok().build();
    }
}