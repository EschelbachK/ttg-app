package com.traintogain.backend.exercise;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseAlternativeController {

    private final ExerciseAlternativesService alternativesService;
    private final SmartAlternativeRankingEngine rankingEngine;
    private final TrainingExerciseRepository repository;

    public ExerciseAlternativeController(
            ExerciseAlternativesService alternativesService,
            SmartAlternativeRankingEngine rankingEngine,
            TrainingExerciseRepository repository
    ) {
        this.alternativesService = alternativesService;
        this.rankingEngine = rankingEngine;
        this.repository = repository;
    }

    @GetMapping("/{exerciseId}/alternatives")
    public ResponseEntity<List<RankedExercise>> getAlternatives(
            @PathVariable String exerciseId
    ) {

        List<TrainingExercise> rawAlternatives =
                alternativesService.getAlternatives(exerciseId);

        List<RankedExercise> ranked =
                rankingEngine.rank(exerciseId, rawAlternatives);

        return ResponseEntity.ok(ranked);
    }

    @GetMapping("/{exerciseId}/alternatives/family")
    public ResponseEntity<List<RankedExercise>> getFamilyAlternatives(
            @PathVariable String exerciseId
    ) {

        List<TrainingExercise> raw =
                alternativesService.getFamilyAlternatives(exerciseId);

        return ResponseEntity.ok(rankingEngine.rank(exerciseId, raw));
    }

    @GetMapping("/{exerciseId}/alternatives/base-pattern")
    public ResponseEntity<List<RankedExercise>> getBasePatternAlternatives(
            @PathVariable String exerciseId
    ) {

        List<TrainingExercise> raw =
                alternativesService.getBasePatternAlternatives(exerciseId);

        return ResponseEntity.ok(rankingEngine.rank(exerciseId, raw));
    }
}