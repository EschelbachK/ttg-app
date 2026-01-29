package com.traintogain.backend.exercise;

import com.traintogain.backend.common.BodyRegion;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "training_exercises")
public class TrainingExercise {

    @Id
    private String id;

    private String folderId;          // Brust-Ordner
    private String name;              // z.B. "Butterfly Maschine"
    private BodyRegion bodyRegion;    // BRUST

    private List<SetEntry> sets = new ArrayList<>();

    // Erweiterungen (kommen später)
    private String notes;
    private Integer restTimerSeconds;

    public TrainingExercise() {
    }

    public TrainingExercise(String folderId, String name, BodyRegion bodyRegion) {
        this.folderId = folderId;
        this.name = name;
        this.bodyRegion = bodyRegion;
    }

    // Getter & Setter

    public String getId() {
        return id;
    }

    public String getFolderId() {
        return folderId;
    }

    public String getName() {
        return name;
    }

    public BodyRegion getBodyRegion() {
        return bodyRegion;
    }

    public List<SetEntry> getSets() {
        return sets;
    }

    public String getNotes() {
        return notes;
    }

    public Integer getRestTimerSeconds() {
        return restTimerSeconds;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBodyRegion(BodyRegion bodyRegion) {
        this.bodyRegion = bodyRegion;
    }

    public void setSets(List<SetEntry> sets) {
        this.sets = sets;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setRestTimerSeconds(Integer restTimerSeconds) {
        this.restTimerSeconds = restTimerSeconds;
    }
}
