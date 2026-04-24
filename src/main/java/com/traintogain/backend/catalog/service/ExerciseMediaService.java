package com.traintogain.backend.catalog.service;

import org.springframework.stereotype.Service;

@Service
public class ExerciseMediaService {

    private static final String BASE_URL = "https://cdn.traintogain.com/exercises/";

    public String getThumbnail(String id, String fallback) {
        return fallback != null ? fallback : BASE_URL + id + "/thumb.webp";
    }

    public String getImage(String id, String fallback) {
        return fallback != null ? fallback : BASE_URL + id + "/image.webp";
    }

    public String getAnimation(String id, String fallback) {
        return fallback != null ? fallback : BASE_URL + id + "/animation.mp4";
    }
}