package com.traintogain.backend.exercise;

import com.traintogain.backend.exception.ForbiddenException;
import com.traintogain.backend.exercise.TrainingExerciseRepository;
import com.traintogain.backend.folder.TrainingFolder;
import com.traintogain.backend.folder.TrainingFolderRepository;
import com.traintogain.backend.training.TrainingPlan;
import com.traintogain.backend.training.TrainingPlanRepository;
import com.traintogain.backend.training.TrainingPlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TrainingPlanServiceTest {

    @Mock
    private TrainingPlanRepository planRepository;

    @Mock
    private TrainingFolderRepository folderRepository;

    @Mock
    private TrainingExerciseRepository exerciseRepository;

    @InjectMocks
    private TrainingPlanService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPlan_success() {
        when(planRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        TrainingPlan plan = service.createPlan("user", "Mein Trainingsplan");

        assertEquals("Mein Trainingsplan", plan.getTitle());
    }

    @Test
    void updatePlan_forbidden() {
        TrainingPlan plan = new TrainingPlan("owner", "Plan");

        when(planRepository.findById("id")).thenReturn(Optional.of(plan));

        assertThrows(ForbiddenException.class,
                () -> service.updatePlan("id", "andererUser", mock()));
    }

    @Test
    void deletePlan_success() {
        TrainingPlan plan = new TrainingPlan("user", "Plan");

        when(planRepository.findById("id")).thenReturn(Optional.of(plan));
        when(folderRepository.findByUserIdAndTrainingPlanIdOrderByOrderAsc(any(), any()))
                .thenReturn(List.of(new TrainingFolder("id", "Brust", null, 0)));

        service.deletePlan("id", "user");

        verify(folderRepository).deleteAll(any());
        verify(planRepository).delete(plan);
    }
}