package com.traintogain.backend.catalog.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSetRequest {

    @Min(0)
    private double weight;

    @Min(1)
    private int reps;

}