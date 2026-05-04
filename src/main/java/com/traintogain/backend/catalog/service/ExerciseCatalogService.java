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
                .map(this::toCatalogResponse)
                .toList();
    }

    public List<ExerciseCatalogResponse> searchExercises(String query) {

        if (query == null || query.isBlank()) return List.of();

        return repository.findByNameContainingIgnoreCase(query).stream()
                .sorted(Comparator.comparing(ExerciseCatalog::getName))
                .map(this::toCatalogResponse)
                .toList();
    }

    public ExerciseCatalogDetailsResponse getExercise(String id) {

        ExerciseCatalog e = getById(id);

        return toDetailsResponse(e);
    }

    public List<ExerciseCatalogResponse> getAlternatives(String id, int limit) {
        ExerciseCatalog current = getById(id);

        int safeLimit = Math.max(1, Math.min(limit, 20));

        return repository.findAll().stream()
                .filter(e -> !Objects.equals(e.getId(), current.getId()))
                .filter(e ->
                        Objects.equals(e.getPrimaryMuscle(), current.getPrimaryMuscle())
                                || Objects.equals(e.getMovementPattern(), current.getMovementPattern())
                                || Objects.equals(e.getBodyRegion(), current.getBodyRegion())
                )
                .sorted(Comparator.comparing(ExerciseCatalog::getName))
                .limit(safeLimit)
                .map(this::toCatalogResponse)
                .toList();
    }

    private ExerciseCatalogResponse toCatalogResponse(ExerciseCatalog e) {
        return ExerciseCatalogResponse.builder()
                .id(e.getId())
                .name(e.getName())
                .bodyRegion(e.getBodyRegion() != null ? e.getBodyRegion().name() : null)
                .family(e.getFamily())
                .movementPattern(e.getMovementPattern() != null ? e.getMovementPattern().name() : null)
                .equipment(mapEquipment(e.getEquipment()))
                .primaryMuscle(e.getPrimaryMuscle() != null ? e.getPrimaryMuscle().name() : null)
                .exerciseType(e.getExerciseType() != null ? e.getExerciseType().name() : null)
                .difficulty(e.getDifficulty() != null ? e.getDifficulty().name() : null)
                .tags(mapTags(e.getTags()))
                .image(buildImage(e))
                .thumbnail(buildThumbnail(e))
                .animation(buildAnimation(e))
                .build();
    }

    private ExerciseCatalogDetailsResponse toDetailsResponse(ExerciseCatalog e) {
        return ExerciseCatalogDetailsResponse.builder()
                .id(e.getId())
                .name(e.getName())
                .bodyRegion(e.getBodyRegion() != null ? e.getBodyRegion().name() : null)
                .family(e.getFamily())
                .movementPattern(e.getMovementPattern() != null ? e.getMovementPattern().name() : null)
                .equipment(mapEquipment(e.getEquipment()))
                .primaryMuscle(e.getPrimaryMuscle() != null ? e.getPrimaryMuscle().name() : null)
                .secondaryMuscles(mapMuscles(e.getSecondaryMuscles()))
                .stabilizers(mapMuscles(e.getStabilizers()))
                .exerciseType(e.getExerciseType() != null ? e.getExerciseType().name() : null)
                .difficulty(e.getDifficulty() != null ? e.getDifficulty().name() : null)
                .tags(mapTags(e.getTags()))
                .image(buildImage(e))
                .thumbnail(buildThumbnail(e))
                .animation(buildAnimation(e))
                .execution(e.getExecution())
                .progression(e.getProgression())
                .safety(e.getSafety())
                .instructions(splitInstructions(e.getInstructions()))
                .tips(e.getTips())
                .commonMistakes(e.getCommonMistakes())
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

        if (sort == null || sort.isBlank()) {
            return list.stream().sorted(byName).toList();
        }

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

    private List<String> mapEquipment(List<EquipmentType> equipment) {
        if (equipment == null) return List.of();

        return equipment.stream()
                .map(Enum::name)
                .toList();
    }

    private List<String> mapMuscles(List<Muscle> muscles) {
        if (muscles == null) return List.of();

        return muscles.stream()
                .map(Enum::name)
                .toList();
    }

    private List<String> mapTags(List<ExerciseTag> tags) {
        if (tags == null) return List.of();

        return tags.stream()
                .map(Enum::name)
                .toList();
    }

    private List<String> splitInstructions(String raw) {
        if (raw == null || raw.isBlank()) return List.of();

        return Arrays.stream(raw.split("\\."))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private String buildImage(ExerciseCatalog e) {
        if (e.getMedia() == null) return null;

        return mediaService.buildImage(
                e.getId(),
                e.getMedia().getImageFile()
        );
    }

    private String buildThumbnail(ExerciseCatalog e) {
        if (e.getMedia() == null) return null;

        return mediaService.buildThumbnail(
                e.getId(),
                e.getMedia().getThumbnailFile()
        );
    }

    private String buildAnimation(ExerciseCatalog e) {
        if (e.getMedia() == null) return null;

        return mediaService.buildAnimation(
                e.getId(),
                e.getMedia().getAnimationFile()
        );
    }
}