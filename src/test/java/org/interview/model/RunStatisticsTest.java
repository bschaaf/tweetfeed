package org.interview.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class RunStatisticsTest {

    @Test
    public void createRunstatisticsForTweetList() {
        List<Tweet> tweetList = new ArrayList<>();
        tweetList.add(new Tweet("1","Thu Jul 02 20:18:19 +0000 2020","tweet tweet", null));
        tweetList.add(new Tweet("2","Thu Jul 02 20:18:22 +0000 2020","tweet tweet", null));
        tweetList.add(new Tweet("3","Thu Jul 02 20:18:29 +0000 2020","tweet tweet", null));
        tweetList.add(new Tweet("4","Thu Jul 02 20:18:31 +0000 2020","tweet tweet", null));
        tweetList.add(new Tweet("5","Thu Jul 02 20:18:32 +0000 2020","tweet tweet", null));
        tweetList.add(new Tweet("6","Thu Jul 02 20:18:39 +0000 2020","tweet tweet", null));

        Long createdAt = Instant.now().getEpochSecond();
        String track="bieber";
        Integer MAX_TWEETS=100;
        Integer MAX_DURATION=30;
        RunStatistics runStatistics = new RunStatistics(tweetList, createdAt, track, MAX_TWEETS, MAX_DURATION);

        Assert.assertEquals(runStatistics.getId(), null);
        Assert.assertEquals(runStatistics.getCreatedAt(), createdAt);
        Assert.assertEquals(runStatistics.getTrack(), "bieber");
        Assert.assertEquals(runStatistics.getFromId(), "1");
        Assert.assertEquals(runStatistics.getToId(), "6");
        Assert.assertEquals(runStatistics.getNtweets(), Integer.valueOf(6));
        Assert.assertEquals(runStatistics.getInterval(), Long.valueOf(20));
        Assert.assertEquals(runStatistics.getAverage(), 0.2f, 0);

        MAX_TWEETS=6;
        runStatistics = new RunStatistics(tweetList, createdAt, track, MAX_TWEETS, MAX_DURATION);
        Assert.assertEquals(runStatistics.getAverage(), 0.3f, 0);
    }
}