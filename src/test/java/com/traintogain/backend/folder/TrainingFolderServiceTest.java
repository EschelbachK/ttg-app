package com.traintogain.backend.folder;

import com.traintogain.backend.exception.ForbiddenException;
import com.traintogain.backend.exception.NotFoundException;
import com.traintogain.backend.exercise.TrainingExercise;
import com.traintogain.backend.exercise.TrainingExerciseRepository;
import com.traintogain.backend.folder.dto.UpdateTrainingFolderRequest;
import com.traintogain.backend.training.TrainingPlan;
import com.traintogain.backend.training.TrainingPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingFolderServiceTest {

    @Mock
    private TrainingFolderRepository folderRepository;

    @Mock
    private TrainingExerciseRepository exerciseRepository;

    @Mock
    private TrainingPlanRepository planRepository;

    @InjectMocks
    private TrainingFolderService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ================= CREATE =================

    @Test
    void createFolder_success() {
        TrainingPlan plan = new TrainingPlan("user", "Plan", 0);

        when(planRepository.findByIdAndUserId("plan", "user"))
                .thenReturn(Optional.of(plan));

        when(folderRepository.findByUserIdAndTrainingPlanIdOrderByOrderAsc("user", "plan"))
                .thenReturn(List.of());

        when(folderRepository.saveAll(any())).thenAnswer(i -> i.getArguments()[0]);

        TrainingFolder result = service.createFolder("user", "plan", "Brust", null);

        assertEquals("Brust", result.getName());
    }

    @Test
    void createFolder_planNotFound() {
        when(planRepository.findByIdAndUserId("plan", "user"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.createFolder("user", "plan", "Brust", null));
    }

    // ================= UPDATE =================

    @Test
    void updateFolder_success() {
        TrainingFolder folder = new TrainingFolder("plan", "Alt", 0);
        folder.setUserId("user");

        when(folderRepository.findById("id")).thenReturn(Optional.of(folder));
        when(folderRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        UpdateTrainingFolderRequest request = mock(UpdateTrainingFolderRequest.class);
        when(request.getName()).thenReturn("Neu");

        TrainingFolder result = service.updateFolder("id", "user", request);

        assertEquals("Neu", result.getName());
    }

    @Test
    void updateFolder_forbidden() {
        TrainingFolder folder = new TrainingFolder("plan", "Alt", 0);
        folder.setUserId("owner");

        when(folderRepository.findById("id")).thenReturn(Optional.of(folder));

        assertThrows(ForbiddenException.class,
                () -> service.updateFolder("id", "user", mock()));
    }

    // ================= DELETE =================

    @Test
    void deleteFolder_success() {
        TrainingFolder folder = new TrainingFolder("plan", "Brust", 0);
        folder.setUserId("user");
        folder.setId("folder");

        when(folderRepository.findById("folder")).thenReturn(Optional.of(folder));
        when(folderRepository.findByUserIdAndTrainingPlanIdOrderByOrderAsc("user", "plan"))
                .thenReturn(List.of(folder));

        service.deleteFolder("folder", "user");

        verify(exerciseRepository).deleteByFolderId("folder");
        verify(folderRepository).delete(folder);
    }

    @Test
    void deleteFolder_forbidden() {
        TrainingFolder folder = new TrainingFolder("plan", "Brust", 0);
        folder.setUserId("owner");

        when(folderRepository.findById("folder")).thenReturn(Optional.of(folder));

        assertThrows(ForbiddenException.class,
                () -> service.deleteFolder("folder", "user"));
    }

    // ================= ARCHIVE =================

    @Test
    void archiveFolder_success() {
        TrainingFolder folder = new TrainingFolder("plan", "Brust", 0);
        folder.setUserId("user");

        when(folderRepository.findById("id")).thenReturn(Optional.of(folder));

        service.archiveFolder("id", "user");

        assertTrue(folder.isArchived());
        verify(folderRepository).save(folder);
    }

    // ================= DUPLICATE =================

    @Test
    void duplicateFolder_success() {
        TrainingFolder original = new TrainingFolder("plan", "Brust", 0);
        original.setUserId("user");
        original.setId("orig");

        when(folderRepository.findById("orig")).thenReturn(Optional.of(original));
        when(folderRepository.findByUserIdAndTrainingPlanIdOrderByOrderAsc("user", "plan"))
                .thenReturn(new java.util.ArrayList<>(List.of(original)));

        when(exerciseRepository.findByFolderId("orig"))
                .thenReturn(List.of(new TrainingExercise()));

        service.duplicateFolder("orig", "user");

        verify(folderRepository).saveAll(any());
        verify(exerciseRepository, atLeastOnce()).save(any());
    }

    // ================= RESTORE =================

    @Test
    void restoreFolder_success() {
        TrainingFolder folder = new TrainingFolder("plan", "Brust", 0);
        folder.setUserId("user");
        folder.setArchived(true);

        when(folderRepository.findById("id")).thenReturn(Optional.of(folder));

        service.restoreFolder("id", "user");

        assertFalse(folder.isArchived());
        verify(folderRepository).save(folder);
    }

    // ================= GET ARCHIVED =================

    @Test
    void getArchivedFolders_success() {
        when(folderRepository.findByUserIdAndArchived("user", true))
                .thenReturn(List.of(new TrainingFolder("plan", "Brust", 0)));

        List<TrainingFolder> result = service.getArchivedFolders("user");

        assertEquals(1, result.size());
    }

    // ================= ORDER =================

    @Test
    void updateOrder_success() {
        TrainingFolder folder = new TrainingFolder("plan", "Brust", 0);
        folder.setUserId("user");
        folder.setId("id");

        when(folderRepository.findById("id")).thenReturn(Optional.of(folder));
        when(folderRepository.findByUserIdAndTrainingPlanIdOrderByOrderAsc("user", "plan"))
                .thenReturn(new java.util.ArrayList<>(List.of(folder)));

        service.updateOrder("id", "user", 0);

        verify(folderRepository).saveAll(any());
    }

    // ================= META =================

    @Test
    void getPlanName_success() {
        TrainingPlan plan = new TrainingPlan("user", "Mein Plan", 0);

        when(planRepository.findById("id")).thenReturn(Optional.of(plan));

        String name = service.getPlanName("id");

        assertEquals("Mein Plan", name);
    }

    @Test
    void isPlanArchived_success() {
        TrainingPlan plan = new TrainingPlan("user", "Plan", 0);
        plan.setArchived(true);

        when(planRepository.findById("id")).thenReturn(Optional.of(plan));

        assertTrue(service.isPlanArchived("id"));
    }
}