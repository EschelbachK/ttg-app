package com.traintogain.backend.catalog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExerciseMediaService {

    @Value("${media.base-url}")
    private String baseUrl;

    @Value("${media.default-image}")
    private String defaultImage;

    @Value("${media.default-animation}")
    private String defaultAnimation;

    @Value("${media.default-thumbnail}")
    private String defaultThumbnail;

    public String getImage(String id, String file) {
        return resolve(id, file, defaultImage);
    }

    public String getAnimation(String id, String file) {
        return resolve(id, file, defaultAnimation);
    }

    public String getThumbnail(String id, String file) {
        return resolve(id, file, defaultThumbnail);
    }

    private String resolve(String id, String file, String fallback) {
        String base = normalize(baseUrl);

        if (file != null && !file.isBlank()) {
            return base + "/exercises/" + id + "/" + file;
        }

        return base + "/" + fallback;
    }

    private String normalize(String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }
}