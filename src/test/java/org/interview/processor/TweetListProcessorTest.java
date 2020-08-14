package org.interview.processor;

import static org.hamcrest.MatcherAssert.assertThat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.interview.model.Tweet;
import org.interview.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class TweetListProcessorTest {
    @Autowired
    TweetListProcessor tweetListProcessor;

    List<Tweet> tweetList = new ArrayList<>();

    @Before
    public void init(){
        User u1 = new User("10", "Fri Jan 03 10:14:18 +0000 2020", "name1", "screenname1");
        User u2 = new User("11", "Thu Jul 02 20:18:19 +0000 2020", "name2", "screenname2");
        User u3 = new User("12", "Thu Sep 12 15:03:19 +0000 2019", "name3", "screenname3");
        tweetList.add(new Tweet("4","Thu Jul 02 20:18:31 +0000 2020","twat twot", u2));
        tweetList.add(new Tweet("6","Thu Jul 02 20:18:39 +0000 2020","tweet tweet", u3));
        tweetList.add(new Tweet("1","Thu Jul 02 20:18:19 +0000 2020","tweet twit", u2));
        tweetList.add(new Tweet("5","Thu Jul 02 20:18:32 +0000 2020","twot tweet", u1));
        tweetList.add(new Tweet("2","Thu Jul 02 20:18:22 +0000 2020","twit twat", u1));
        tweetList.add(new Tweet("3","Thu Jul 02 20:18:29 +0000 2020","twat twut", u1));
        tweetList.sort(Tweet.TweetComparator);
    }

    @Test
    public void sortTweetListAndCheckOrderTest(){
        tweetListProcessor.sort(tweetList);
        Assert.assertEquals("1", tweetList.get(0).getId());
        Assert.assertEquals("2", tweetList.get(1).getId());
        Assert.assertEquals("3", tweetList.get(2).getId());
        Assert.assertEquals("4", tweetList.get(3).getId());
        Assert.assertEquals("5", tweetList.get(4).getId());
        Assert.assertEquals("6", tweetList.get(5).getId());
    }

    @Test
    public void saveTweetListAndCompareWithReturnedTest(){  
        var list = tweetListProcessor
                    .configure("bieber")
                    .sort(tweetList)
                    .saveTweetList(tweetList)
                    .block();
        assertThat(list, CoreMatchers.hasItems(
            tweetList.get(0),
            tweetList.get(1),
            tweetList.get(2),
            tweetList.get(3),
            tweetList.get(4),
            tweetList.get(5)       
            ));
    }  

    @Test
    public void saveRunStatisticsAndCompareWithReturned(){
        Long timeStamp = Instant.now().getEpochSecond();

        var stats = tweetListProcessor
                        .configure("bieber")
                        .sort(tweetList)
                        .saveRunStatistics(tweetList, timeStamp)
                        .block();
        
        Assert.assertEquals(timeStamp, stats.getCreatedAt());
        Assert.assertEquals("bieber", stats.getTrack());
        Assert.assertEquals("1", stats.getFromId());
        Assert.assertEquals("6", stats.getToId());
        Assert.assertEquals(6, stats.getNtweets(), 6);
        Assert.assertEquals(20, stats.getInterval(), 20);
        Assert.assertEquals(0.2f, stats.getAverage(), 0);
    } 
}