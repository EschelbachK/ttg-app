package com.traintogain.backend.exercise;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.traintogain.backend.catalog.model.SpeedType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "training_exercises")
public class TrainingExercise {

    @Id
    private String id;

    private String exerciseId;
    private String userId;
    private String folderId;

    private String name;
    private String bodyRegion;
    private String family;
    private String movementPattern;
    private String equipment;
    private String primaryMuscle;

    private List<String> secondaryMuscles = new ArrayList<>();

    @JsonProperty("speedType")
    private SpeedType speedType;

    private List<String> stabilizers = new ArrayList<>();
    private String exerciseType;
    private String difficulty;

    private List<String> tags = new ArrayList<>();

    private Media media = new Media();
    private Execution execution = new Execution();
    private Progression progression = new Progression();
    private Safety safety = new Safety();

    private List<String> instructions = new ArrayList<>();
    private List<String> tips = new ArrayList<>();
    private List<String> commonMistakes = new ArrayList<>();

    private List<SetEntry> sets = new ArrayList<>();

    private int orderIndex;

    private Instant createdAt;
    private Instant updatedAt;

    @JsonProperty("catalogId")
    public String getCatalogId() {
        return exerciseId;
    }

    @JsonProperty("catalogId")
    public void setCatalogId(String catalogId) {
        this.exerciseId = catalogId;
    }

    public void prePersist() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    public void preUpdate() {
        updatedAt = Instant.now();
    }

    public TrainingExercise copyForFolder(String newFolderId) {
        TrainingExercise copy = new TrainingExercise();
        copy.setUserId(userId);
        copy.setFolderId(newFolderId);
        copy.setExerciseId(exerciseId);

        copy.setSets(sets == null
                ? new ArrayList<>()
                : sets.stream()
                .map(s -> new SetEntry(s.getWeight(), s.getRepetitions()))
                .toList());

        copy.prePersist();
        return copy;
    }

    @Getter
    @Setter
    public static class Media {
        private String image = "";
        private String thumbnail = "";
        private String animation = "";
    }

    @Getter
    @Setter
    public static class Execution {
        private String tempo;
        private String rangeOfMotion;
    }

    @Getter
    @Setter
    public static class Progression {
        private List<String> regressions = new ArrayList<>();
        private List<String> progressions = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class Safety {
        private String riskLevel;
        private List<String> contraindications = new ArrayList<>();
    }
}