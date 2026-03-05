package com.traintogain.backend.workout.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "workout_sessions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSession {

    @Id
    private String id;

    private String userId;

    private Instant startedAt;

    private Instant finishedAt;

    private List<ExerciseSession> exercises;

}