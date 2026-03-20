package com.traintogain.backend.training;

import com.traintogain.backend.exception.ForbiddenException;
import com.traintogain.backend.exception.NotFoundException;
import com.traintogain.backend.exercise.TrainingExerciseRepository;
import com.traintogain.backend.folder.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

        TrainingPlan plan = service.createPlan("user", "Mein Plan");

        assertEquals("Mein Plan", plan.getTitle());
    }

    @Test
    void updatePlan_notFound() {
        when(planRepository.findById("id")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.updatePlan("id", "user", mock()));
    }

    @Test
    void updatePlan_forbidden() {
        TrainingPlan plan = new TrainingPlan("owner", "Plan");

        when(planRepository.findById("id")).thenReturn(Optional.of(plan));

        assertThrows(ForbiddenException.class,
                () -> service.updatePlan("id", "user", mock()));
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

    @Test
    void deletePlan_forbidden() {
        TrainingPlan plan = new TrainingPlan("owner", "Plan");

        when(planRepository.findById("id")).thenReturn(Optional.of(plan));

        assertThrows(ForbiddenException.class,
                () -> service.deletePlan("id", "user"));
    }
}