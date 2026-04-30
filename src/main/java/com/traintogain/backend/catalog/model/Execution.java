package com.traintogain.backend.catalog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Execution {

    private String tempo;

    private RangeOfMotion rangeOfMotion;

    private SpeedType speedType;
}