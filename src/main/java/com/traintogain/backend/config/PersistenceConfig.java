package com.traintogain.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.traintogain.backend.user"
)
@EnableMongoRepositories(
        basePackages = {
                "com.traintogain.backend.exercise",
                "com.traintogain.backend.training",
                "com.traintogain.backend.folder",
                "com.traintogain.backend.auth.refreshtoken",
                "com.traintogain.backend.passwordreset",
                "com.traintogain.backend.catalog",
                "com.traintogain.backend.workout"
        }
)
public class PersistenceConfig {
}
