package com.traintogain.backend.workout.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetLog {

    private double weight;

    private int reps;

}