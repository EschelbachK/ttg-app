package com.traintogain.backend.exercise;

import com.traintogain.backend.exercise.dto.RankedExerciseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseAlternativeController {

    private final ExerciseAlternativesService alternativesService;
    private final SmartAlternativeRankingEngine rankingEngine;

    public ExerciseAlternativeController(
            ExerciseAlternativesService alternativesService,
            SmartAlternativeRankingEngine rankingEngine
    ) {
        this.alternativesService = alternativesService;
        this.rankingEngine = rankingEngine;
    }

    @GetMapping("/{exerciseId}/alternatives")
    public ResponseEntity<List<RankedExerciseResponse>> getAlternatives(@PathVariable String exerciseId) {
        List<RankedExerciseResponse> ranked =
                rankingEngine.rank(exerciseId, alternativesService.getAlternatives(exerciseId));
        return ResponseEntity.ok(ranked);
    }

    @GetMapping("/{exerciseId}/alternatives/family")
    public ResponseEntity<List<RankedExerciseResponse>> getFamilyAlternatives(@PathVariable String exerciseId) {
        List<RankedExerciseResponse> ranked =
                rankingEngine.rank(exerciseId, alternativesService.getFamilyAlternatives(exerciseId));
        return ResponseEntity.ok(ranked);
    }

    @GetMapping("/{exerciseId}/alternatives/base-pattern")
    public ResponseEntity<List<RankedExerciseResponse>> getBasePatternAlternatives(@PathVariable String exerciseId) {
        List<RankedExerciseResponse> ranked =
                rankingEngine.rank(exerciseId, alternativesService.getBasePatternAlternatives(exerciseId));
        return ResponseEntity.ok(ranked);
    }
}