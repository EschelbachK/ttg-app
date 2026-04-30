package com.traintogain.backend.catalog.model;

import lombok.*;

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