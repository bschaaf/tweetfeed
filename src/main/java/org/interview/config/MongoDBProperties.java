package org.interview.config;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "mongo")
public class MongoDBProperties {
    @NotBlank
    private String db;
    @NotBlank
    private String uri;
}