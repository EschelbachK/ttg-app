package com.traintogain.backend.catalog.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EnumOptionResponse {

    private String key;
    private String label;
}