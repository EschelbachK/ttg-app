package com.traintogain.backend.catalog.service;

import com.traintogain.backend.catalog.dto.ExerciseCatalogDetailsResponse;
import com.traintogain.backend.catalog.dto.ExerciseCatalogResponse;
import com.traintogain.backend.catalog.dto.ExerciseFilterRequest;
import com.traintogain.backend.catalog.logic.ExerciseFilterEngine;
import com.traintogain.backend.catalog.model.*;
import com.traintogain.backend.catalog.repository.ExerciseCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExerciseCatalogService {

    private final ExerciseCatalogRepository repository;
    private final ExerciseMediaService mediaService;

    public List<BodyRegion> getCategories() {
        return List.of(BodyRegion.values());
    }

    public ExerciseCatalog getById(String id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Exercise not found"));
    }

    public List<ExerciseCatalogResponse> getExercises(ExerciseFilterRequest filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        List<ExerciseCatalog> base = repository.findAll();
        List<ExerciseCatalog> filtered = ExerciseFilterEngine.filter(
                base,
                filter.getBodyRegion(),
                filter.getMuscle(),
                filter.getPattern(),
                filter.getEquipment()
        );

        if (filter.getTags() != null && !filter.getTags().isEmpty()) {
            Set<ExerciseTag> tagSet = new HashSet<>(filter.getTags());
            filtered = filtered.stream().filter(e -> e.getTags().stream().anyMatch(tagSet::contains)).toList();
        }

        filtered = applySort(filtered, filter.getSort());
        int total = filtered.size();
        int from = Math.min((int) pageable.getOffset(), total);
        int to = Math.min(from + pageable.getPageSize(), total);
        if (from >= to) return List.of();

        return filtered.subList(from, to).stream().map(this::mapToResponse).toList();
    }

    public List<ExerciseCatalogResponse> searchExercises(String query) {
        if (query == null || query.isBlank()) return List.of();
        String q = query.toLowerCase();
        return repository.findAll().stream()
                .filter(e -> e.getName() != null && e.getName().toLowerCase().contains(q))
                .sorted(Comparator.comparing(ExerciseCatalog::getName))
                .map(this::mapToResponse)
                .toList();
    }

    private List<ExerciseCatalog> applySort(List<ExerciseCatalog> list, String sort) {
        Comparator<ExerciseCatalog> comparator = Comparator.comparing(ExerciseCatalog::getName);
        if (sort == null) return list.stream().sorted(comparator).toList();
        return switch (sort) {
            case "name_desc" -> list.stream().sorted(comparator.reversed()).toList();
            case "difficulty" -> list.stream().sorted(
                    Comparator.comparing(ExerciseCatalog::getDifficulty, Comparator.nullsLast(Comparator.naturalOrder()))
            ).toList();
            default -> list.stream().sorted(comparator).toList();
        };
    }

    public ExerciseCatalogDetailsResponse getExercise(String id) {
        var e = getById(id);
        return ExerciseCatalogDetailsResponse.builder()
                .id(e.getId())
                .name(e.getName())
                .bodyRegion(e.getBodyRegion())
                .family(e.getFamily())
                .movementPattern(e.getMovementPattern())
                .basePattern(e.getBasePattern())
                .equipment(e.getEquipment())
                .primaryMuscle(e.getPrimaryMuscle())
                .secondaryMuscles(e.getSecondaryMuscles())
                .stabilizers(e.getStabilizers())
                .exerciseType(e.getExerciseType())
                .difficulty(e.getDifficulty())
                .tags(mapTags(e.getTags()))
                .media(resolveMedia(e))
                .execution(e.getExecution())
                .progression(e.getProgression())
                .safety(e.getSafety())
                .instructions(e.getInstructions())
                .tips(e.getTips())
                .commonMistakes(e.getCommonMistakes())
                .build();
    }

    private ExerciseCatalogResponse mapToResponse(ExerciseCatalog e) {
        return ExerciseCatalogResponse.builder()
                .id(e.getId())
                .name(e.getName())
                .bodyRegion(e.getBodyRegion())
                .family(e.getFamily())
                .movementPattern(e.getMovementPattern())
                .basePattern(e.getBasePattern())
                .equipment(e.getEquipment())
                .primaryMuscle(e.getPrimaryMuscle())
                .exerciseType(e.getExerciseType())
                .difficulty(e.getDifficulty())
                .tags(mapTags(e.getTags()))
                .media(resolveMedia(e))
                .build();
    }

    private List<String> mapTags(List<ExerciseTag> tags) {
        if (tags == null || tags.isEmpty()) return List.of();
        return tags.stream().map(Enum::name).toList();
    }

    private ExerciseMedia resolveMedia(ExerciseCatalog e) {
        ExerciseMedia media = e.getMedia();
        if (media == null) throw new IllegalStateException("media missing for exercise: " + e.getId());
        if (isBlank(media.getImage())) throw new IllegalStateException("image missing for exercise: " + e.getId());
        if (isBlank(media.getThumbnail())) throw new IllegalStateException("thumbnail missing for exercise: " + e.getId());
        if (isBlank(media.getAnimation())) throw new IllegalStateException("animation missing for exercise: " + e.getId());

        return ExerciseMedia.builder()
                .image(mediaService.getImage(e.getId(), media.getImage()))
                .thumbnail(mediaService.getThumbnail(e.getId(), media.getThumbnail()))
                .animation(mediaService.getAnimation(e.getId(), media.getAnimation()))
                .build();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}