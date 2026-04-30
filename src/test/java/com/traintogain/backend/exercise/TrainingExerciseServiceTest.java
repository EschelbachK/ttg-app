package com.traintogain.backend.exercise;

import com.traintogain.backend.catalog.model.ExerciseCatalog;
import com.traintogain.backend.catalog.service.ExerciseCatalogService;
import com.traintogain.backend.exception.ForbiddenException;
import com.traintogain.backend.exception.NotFoundException;
import com.traintogain.backend.exercise.dto.CreateTrainingExerciseRequest;
import com.traintogain.backend.exercise.dto.SetEntryRequest;
import com.traintogain.backend.folder.TrainingFolder;
import com.traintogain.backend.folder.TrainingFolderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingExerciseServiceTest {

    @Mock
    private TrainingExerciseRepository exerciseRepository;

    @Mock
    private TrainingFolderRepository folderRepository;

    @Mock
    private ExerciseCatalogService catalogService;

    @Mock
    private ExerciseValidationService validationService;

    @InjectMocks
    private TrainingExerciseService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addExercise_success() {
        TrainingFolder folder = new TrainingFolder("plan", "Brust", 0);
        folder.setUserId("user");

        when(folderRepository.findById("folder")).thenReturn(Optional.of(folder));

        ExerciseCatalog catalog = new ExerciseCatalog();
        catalog.setId("Bankdrücken");
        catalog.setFamily(ExerciseFamily.valueOf("PUSH"));
        catalog.setBasePattern(BaseMovementPattern.HORIZONTAL_PUSH);
        when(catalogService.getById("Bankdrücken")).thenReturn(catalog);

        when(exerciseRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        CreateTrainingExerciseRequest request = new CreateTrainingExerciseRequest(
                "Bankdrücken",
                List.of(new SetEntryRequest(100.0, 10))
        );

        TrainingExercise result = service.addExercise("user", "plan", "folder", request);

        assertEquals(request.exerciseId(), result.getExerciseId());
        assertEquals(request.sets().size(), result.getSets().size());
        assertEquals(request.sets().get(0).weight(), result.getSets().get(0).getWeight());
        assertEquals(request.sets().get(0).repetitions(), result.getSets().get(0).getRepetitions());

        verify(exerciseRepository).save(any());
    }

    @Test
    void addExercise_forbidden() {
        TrainingFolder folder = new TrainingFolder("plan", "Brust", 0);
        folder.setUserId("owner");

        when(folderRepository.findById("folder")).thenReturn(Optional.of(folder));

        CreateTrainingExerciseRequest request = new CreateTrainingExerciseRequest(
                "Bankdrücken",
                List.of(new SetEntryRequest(100.0, 10))
        );

        assertThrows(ForbiddenException.class,
                () -> service.addExercise("user", "plan", "folder", request));
    }

    @Test
    void getExercises_success() {
        TrainingFolder folder = new TrainingFolder("plan", "Brust", 0);
        folder.setUserId("user");
        folder.setId("folder"); // <-- fix

        when(folderRepository.findById("folder")).thenReturn(Optional.of(folder));

        Page<TrainingExercise> page = new PageImpl<>(List.of(new TrainingExercise()));
        when(exerciseRepository.findByUserIdAndFolderId(eq("user"), eq("folder"), any()))
                .thenReturn(page);

        Page<TrainingExercise> result = service.getExercisesByFolder(
                "user", "plan", "folder", PageRequest.of(0, 10)
        );

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void getExercises_notFound() {
        when(folderRepository.findById("folder")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.getExercisesByFolder("user", "plan", "folder", PageRequest.of(0, 10)));
    }

    @Test
    void getExercises_forbidden() {
        TrainingFolder folder = new TrainingFolder("plan", "Brust", 0);
        folder.setUserId("owner");

        when(folderRepository.findById("folder")).thenReturn(Optional.of(folder));

        assertThrows(ForbiddenException.class,
                () -> service.getExercisesByFolder("user", "plan", "folder", PageRequest.of(0, 10)));
    }

    @Test
    void deleteExercise_notFound() {
        when(exerciseRepository.findById("id")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.deleteExercise("id", "user"));
    }

    @Test
    void deleteExercise_forbidden() {
        TrainingExercise ex = new TrainingExercise();
        ex.setUserId("owner");

        when(exerciseRepository.findById("id")).thenReturn(Optional.of(ex));

        assertThrows(ForbiddenException.class,
                () -> service.deleteExercise("id", "user"));
    }
}