package com.traintogain.backend.catalog.service;

import com.traintogain.backend.catalog.dto.ExerciseCatalogDetailsResponse;
import com.traintogain.backend.catalog.dto.ExerciseCatalogResponse;
import com.traintogain.backend.catalog.dto.ExerciseFilterRequest;
import com.traintogain.backend.catalog.logic.ExerciseFilterService;
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
            filtered = applyTagFilter(filtered, filter.getTags());
        }

        filtered = applySort(filtered, filter.getSort());

        return paginate(filtered, filter.getPage(), filter.getSize())
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

    public ExerciseCatalogDetailsResponse getExercise(String id) {

        ExerciseCatalog e = getById(id);

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
                .image(resolveImage(e))
                .thumbnail(resolveThumbnail(e))
                .animation(resolveAnimation(e))
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
                .image(resolveImage(e))
                .thumbnail(resolveThumbnail(e))
                .animation(resolveAnimation(e))
                .build();
    }

    private List<ExerciseCatalog> applyTagFilter(List<ExerciseCatalog> list, List<ExerciseTag> tags) {

        Set<ExerciseTag> tagSet = new HashSet<>(tags);

        return list.stream()
                .filter(e -> e.getTags() != null && e.getTags().stream().anyMatch(tagSet::contains))
                .toList();
    }

    private List<ExerciseCatalog> applySort(List<ExerciseCatalog> list, String sort) {

        Comparator<ExerciseCatalog> byName = Comparator.comparing(ExerciseCatalog::getName);

        if (sort == null) return list.stream().sorted(byName).toList();

        return switch (sort) {
            case "name_desc" -> list.stream().sorted(byName.reversed()).toList();

            case "difficulty" -> list.stream().sorted(
                    Comparator.comparing(
                            ExerciseCatalog::getDifficulty,
                            Comparator.nullsLast(Comparator.naturalOrder())
                    )
            ).toList();

            default -> list.stream().sorted(byName).toList();
        };
    }

    private List<ExerciseCatalog> paginate(List<ExerciseCatalog> list, int page, int size) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);

        int total = list.size();
        int from = Math.min(safePage * safeSize, total);
        int to = Math.min(from + safeSize, total);

        if (from >= to) return List.of();

        return list.subList(from, to);
    }

    private List<String> mapTags(List<ExerciseTag> tags) {

        if (tags == null || tags.isEmpty()) return List.of();

        return tags.stream()
                .map(Enum::name)
                .toList();
    }

    private String resolveImage(ExerciseCatalog e) {
        return resolveMedia(e, MediaType.IMAGE);
    }

    private String resolveThumbnail(ExerciseCatalog e) {
        return resolveMedia(e, MediaType.THUMBNAIL);
    }

    private String resolveAnimation(ExerciseCatalog e) {
        return resolveMedia(e, MediaType.ANIMATION);
    }

    private String resolveMedia(ExerciseCatalog e, MediaType type) {

        if (e.getMedia() == null) return null;

        return switch (type) {

            case IMAGE -> blank(e.getMedia().getImageFile())
                    ? null
                    : mediaService.buildImage(e.getId(), e.getMedia().getImageFile());

            case THUMBNAIL -> blank(e.getMedia().getThumbnailFile())
                    ? null
                    : mediaService.buildThumbnail(e.getId(), e.getMedia().getThumbnailFile());

            case ANIMATION -> blank(e.getMedia().getAnimationFile())
                    ? null
                    : mediaService.buildAnimation(e.getId(), e.getMedia().getAnimationFile());
        };
    }

    private boolean blank(String v) {
        return v == null || v.isBlank();
    }

    private enum MediaType {
        IMAGE, THUMBNAIL, ANIMATION
    }
}