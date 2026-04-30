package com.traintogain.backend.catalog.service;

import com.traintogain.backend.catalog.logic.ExerciseFilterService;
import com.traintogain.backend.catalog.dto.ExerciseCatalogDetailsResponse;
import com.traintogain.backend.catalog.dto.ExerciseCatalogResponse;
import com.traintogain.backend.catalog.dto.ExerciseFilterRequest;
import com.traintogain.backend.catalog.model.*;
import com.traintogain.backend.catalog.repository.ExerciseCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ExerciseCatalogService {

    private final ExerciseCatalogRepository repository;
    private final ExerciseFilterService filterService;
    private final ExerciseMediaService mediaService;

    public List<BodyRegion> getCategories() {
        return List.of(BodyRegion.values());
    }

    public ExerciseCatalog getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("exercise not found"));
    }

    public List<ExerciseCatalogResponse> getExercises(ExerciseFilterRequest filter) {
        List<ExerciseCatalog> base = repository.findAll();

        List<ExerciseCatalog> filtered = filterService.filter(
                base,
                filter.getBodyRegions(),
                filter.getMuscles(),
                filter.getPatterns(),
                filter.getEquipment(),
                filter.getPlanes(),
                filter.getMechanics(),
                filter.getLoadTypes(),
                filter.getLateralities()
        );

        if (filter.getTags() != null && !filter.getTags().isEmpty()) {
            Set<ExerciseTag> tagSet = new HashSet<>(filter.getTags());
            filtered = filtered.stream()
                    .filter(e -> e.getTags() != null && e.getTags().stream().anyMatch(tagSet::contains))
                    .toList();
        }

        filtered = applySort(filtered, filter.getSort());

        int total = filtered.size();
        int from = Math.min(filter.getPage() * filter.getSize(), total);
        int to = Math.min(from + filter.getSize(), total);
        if (from >= to) return List.of();

        return filtered.subList(from, to)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ExerciseCatalogResponse> searchExercises(String query) {
        if (query == null || query.isBlank()) return List.of();

        return repository.findByNameContainingIgnoreCase(query).stream()
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
                    Comparator.comparing(
                            ExerciseCatalog::getDifficulty,
                            Comparator.nullsLast(Comparator.naturalOrder())
                    )
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
                .equipment(e.getEquipment())
                .primaryMuscle(e.getPrimaryMuscle())
                .secondaryMuscles(e.getSecondaryMuscles())
                .stabilizers(e.getStabilizers())
                .exerciseType(e.getExerciseType())
                .difficulty(e.getDifficulty())
                .tags(mapTags(e.getTags()))
                .image(buildImage(e))
                .thumbnail(buildThumbnail(e))
                .animation(buildAnimation(e))
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
                .equipment(e.getEquipment())
                .primaryMuscle(e.getPrimaryMuscle())
                .exerciseType(e.getExerciseType())
                .difficulty(e.getDifficulty())
                .tags(mapTags(e.getTags()))
                .image(buildImage(e))
                .thumbnail(buildThumbnail(e))
                .animation(buildAnimation(e))
                .build();
    }

    private List<String> mapTags(List<ExerciseTag> tags) {
        if (tags == null || tags.isEmpty()) return List.of();
        return tags.stream().map(Enum::name).toList();
    }

    private String buildImage(ExerciseCatalog e) {
        if (e.getMedia() == null || isBlank(e.getMedia().getImageFile())) return null;
        return mediaService.buildImage(e.getId(), e.getMedia().getImageFile());
    }

    private String buildThumbnail(ExerciseCatalog e) {
        if (e.getMedia() == null || isBlank(e.getMedia().getThumbnailFile())) return null;
        return mediaService.buildThumbnail(e.getId(), e.getMedia().getThumbnailFile());
    }

    private String buildAnimation(ExerciseCatalog e) {
        if (e.getMedia() == null || isBlank(e.getMedia().getAnimationFile())) return null;
        return mediaService.buildAnimation(e.getId(), e.getMedia().getAnimationFile());
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}