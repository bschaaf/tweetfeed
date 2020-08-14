package org.interview.model;

import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.interview.model.ModelUtil.CustomValueSerializer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data class representing a Twitter user
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    public User(String id, String createdAt, String name, String screenName) {
        this.id = id;
        this.name = name;
        this.screenName = screenName;
        setCreatedAt(createdAt);
    }

    @JsonProperty("id_str")
    private String id;

    @JsonProperty("created_at")
    @JsonSerialize(using = CustomValueSerializer.class)
    private Long createdAt;

    @JsonProperty("name")
    private String name;

    @JsonProperty("screen_name")
    private String screenName;

    // Convert Twitter date string to epoch seconds
    public void setCreatedAt(String date) {
        this.createdAt = ModelUtil.setCreatedAtDateEpoch(date);
    }

    public Long getCreatedAt() {
        return this.createdAt;
    }

    public static Comparator<User> CreatedAtComparator = new Comparator<User>() {
        @Override
        public int compare(User u1, User u2) {
            return u1.getCreatedAt().compareTo(u2.getCreatedAt());
        }
    };

    @Override
    public String toString() {
        return "User{" + "id='" + id + "\'" + ", createdAt='" + getCreatedAt() + "\'" + ", name='" + name + "\'"
                + ", screenName='" + screenName + "\'" + '}';
    }

    public String summary() {
        return "User created at " + getCreatedAt() + ": " + screenName;
    }
}