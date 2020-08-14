package org.interview.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * Data class collecting statistics for one run  
 */ 
@Document(collection = "statistics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RunStatistics {
    private String id;
    private Long createdAt;
    private String track;
    private String fromId;
    private String toId;
    private Integer ntweets;
    private Long interval;
    private Float average;

    public RunStatistics(List<Tweet> tweetList, long createdAt, String track, final int MAX_TWEETS, final int MAX_DURATION){
        this.createdAt=createdAt;
        this.track=track;
        var listSize = tweetList.size();
        var first = tweetList.get(0);
        var last = tweetList.get(listSize-1);
        this.fromId = first.getId();
        this.toId = last.getId();
        this.ntweets = tweetList.size();
        this.interval = Math.abs(first.getCreatedAt() - last.getCreatedAt());
        // Use observed interval to calculate average in case MAX_TWEETS is reached, otherwise use MAX_Duration
        this.average = (this.ntweets == MAX_TWEETS) ? (float)this.ntweets/this.interval : (float)this.ntweets/MAX_DURATION;
    }

    @Override
    public String toString() {
        return String.format("Statistics for track: %s, single run at: %d, average tweet per second: %.2f", 
                                    track, createdAt, average);
    }
}



