package org.interview.model;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class RunStatisticsRepositoryTest {

    @Autowired 
    RunStatisticsRepository runStatisticsRepository;

    @Test
    public void saveRunStatisticsToRepositoryAndCompareWithReturned() {
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
        runStatisticsRepository
            .save(runStatistics)
            .subscribe(saved -> {
                Assert.assertNotNull(saved.getId());
                Assert.assertEquals(saved.getCreatedAt(), createdAt);
                Assert.assertEquals(saved.getFromId(), "1");
                Assert.assertEquals(saved.getToId(), "6");
                Assert.assertEquals(saved.getTrack(), track);
                Assert.assertEquals(saved.getInterval(), Long.valueOf(20));
                Assert.assertEquals(saved.getNtweets(), Integer.valueOf(6));
                Assert.assertEquals(saved.getAverage(), 0.2f, 0);
            });
    }
    
}