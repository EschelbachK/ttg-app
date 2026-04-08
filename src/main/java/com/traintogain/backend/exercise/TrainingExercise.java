package com.traintogain.backend.exercise;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Document(collection = "training_exercises")
public class TrainingExercise {

    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String folderId;

    private String name;

    private List<SetEntry> sets;

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getFolderId() { return folderId; }
    public String getName() { return name; }
    public List<SetEntry> getSets() { return sets; }

    public void setId(String id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setFolderId(String folderId) { this.folderId = folderId; }
    public void setName(String name) { this.name = name; }
    public void setSets(List<SetEntry> sets) { this.sets = sets; }

    public TrainingExercise copyForFolder(String newFolderId) {
        TrainingExercise copy = new TrainingExercise();
        copy.setUserId(this.userId);
        copy.setFolderId(newFolderId);
        copy.setName(this.name);
        copy.setSets(this.sets); // optional deep copy später
        return copy;
    }
}