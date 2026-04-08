package com.traintogain.backend.folder.dto;

import jakarta.validation.constraints.Min;

public class UpdateTrainingFolderRequest {

    private String name;

    @Min(value = 0, message = "Reihenfolge muss >= 0 sein")
    private Integer order;

    public String getName() { return name; }
    public Integer getOrder() { return order; }
}