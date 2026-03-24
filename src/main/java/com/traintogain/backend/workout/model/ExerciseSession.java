package com.traintogain.backend.workout.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseSession {

    private String catalogExerciseId;

    private String name;

    private List<SetLog> sets;
}