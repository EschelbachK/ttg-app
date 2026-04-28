package com.traintogain.backend.exercise;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "training_exercises")
@CompoundIndex(name = "user_folder_idx", def = "{'userId': 1, 'folderId': 1}")
public class TrainingExercise {

    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String folderId;

    @Indexed
    private String exerciseId; // <- wichtig: entspricht der "name" aus Test

    private List<SetEntry> sets = new ArrayList<>();

    private int orderIndex;

    private Instant createdAt;
    private Instant updatedAt;

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
        copy.setSets(sets.stream()
                .map(s -> new SetEntry(s.getWeight(), s.getRepetitions()))
                .toList());
        copy.prePersist();
        return copy;
    }
}