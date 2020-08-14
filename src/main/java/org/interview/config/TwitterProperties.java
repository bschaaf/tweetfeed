package org.interview.config;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "twitter")
public class TwitterProperties {
    @NotBlank
    private String consumer_key;
    @NotBlank
    private String consumer_secret;
    @NotBlank
    private String base_url;
    
	private int max_tweets;
	private int max_duration;
}