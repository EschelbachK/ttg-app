package com.traintogain.backend.catalog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExerciseMediaService {

    @Value("${media.base-url}")
    private String baseUrl;

    public String getImage(String id, String file) {
        return resolve(id, file);
    }

    public String getAnimation(String id, String file) {
        return resolve(id, file);
    }

    public String getThumbnail(String id, String file) {
        return resolve(id, file);
    }

    private String resolve(String id, String file) {
        if (isBlank(baseUrl)) throw new IllegalStateException("media.base-url missing");
        if (isBlank(id)) throw new IllegalStateException("exercise id missing for media");
        if (isBlank(file)) throw new IllegalStateException("media file missing for exercise: " + id);
        return build(baseUrl, "exercises/" + id + "/" + file);
    }

    private String build(String base, String path) {
        String cleanBase = base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
        String cleanPath = path.startsWith("/") ? path.substring(1) : path;
        return cleanBase + "/" + cleanPath;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}