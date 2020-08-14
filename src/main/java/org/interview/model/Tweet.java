package org.interview.model;

import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.interview.model.ModelUtil.CustomValueSerializer;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Data class representing a Twitter tweet
 */
@Log4j2
@Document(collection = "tweets")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Tweet {
    public Tweet(String id, String createdAt, String text, User user) {
        this.id = id;
        this.text = text;
        this.user = user;
        setCreatedAt(createdAt);
    }

    @JsonProperty("id_str")
    private String id;

    @JsonProperty("created_at")
    @JsonSerialize(using = CustomValueSerializer.class)
    private Long createdAt;

    @JsonProperty("text")
    private String text;

    @JsonProperty("user")
    private User user;

    // Convert Twitter date string to epoch seconds
    public void setCreatedAt(String date) {
        this.createdAt = ModelUtil.setCreatedAtDateEpoch(date);
    }

    public Long getCreatedAt() {
        return this.createdAt;
    }

    public static Comparator<Tweet> TweetComparator = new Comparator<Tweet>() {
        @Override
        public int compare(Tweet t1, Tweet t2) {
            return t1.getId().compareTo(t2.getId());
        }
    };

    public static Comparator<Tweet> CreatedAtComparator = new Comparator<Tweet>() {
        @Override
        public int compare(Tweet t1, Tweet t2) {
            return t1.getCreatedAt().compareTo(t2.getCreatedAt());
        }
    };

    @Override
    public String toString() {
        return "Tweet{" + "id='" + id + "\'" + ", createdAt='" + getCreatedAt() + "\'" + ", text='"
                + (text.length() > 29 ? (text.substring(0, 29) + "...\'") : text + "\'") + ", user=" + user + '}';
    }

    public String summary() {
        return "Tweet created at " + getCreatedAt() + ": "
                + (text.length() > 29 ? (text.substring(0, 29) + "...\'") : text);
    }

    public String jsonString() {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.info("Problem processing json: " + e.getMessage());
        }
        return json;
    }
}