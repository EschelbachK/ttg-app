package com.traintogain.backend.catalog.service;

import com.traintogain.backend.catalog.model.ExerciseCatalog;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ExerciseSearchService {

    public List<ExerciseCatalog> search(List<ExerciseCatalog> source, String query) {

        if (query == null || query.isBlank()) {
            return List.of();
        }

        String q = query.toLowerCase().trim();

        return source.stream()
                .map(e -> new Scored(e, score(e, q)))
                .filter(s -> s.score > 0)
                .sorted(Comparator.comparingInt(Scored::score).reversed())
                .map(Scored::exercise)
                .toList();
    }

    private int score(ExerciseCatalog e, String q) {

        int score = 0;

        String name = safe(e.getName());
        String primary = safeEnum(e.getPrimaryMuscle());
        String pattern = safeEnum(e.getMovementPattern());
        String body = safeEnum(e.getBodyRegion());

        if (name.startsWith(q)) score += 50;
        if (name.contains(q)) score += 30;

        if (primary.contains(q)) score += 20;
        if (pattern.contains(q)) score += 15;
        if (body.contains(q)) score += 10;

        return score;
    }

    private String safe(String s) {
        return s == null ? "" : s.toLowerCase();
    }

    private String safeEnum(Enum<?> e) {
        return e == null ? "" : e.name().toLowerCase();
    }

    private record Scored(ExerciseCatalog exercise, int score) {}
}