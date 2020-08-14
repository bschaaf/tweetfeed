package org.interview.model;

import org.junit.Assert;
import org.junit.Test;

public class TweetTest {

    @Test
    public void createTweetTest(){
        Tweet tweet = new Tweet("1","Thu Jul 02 20:18:19 +0000 2020","tweet tweet", new User());
        Assert.assertEquals(tweet.getId(), "1");
        Assert.assertEquals(tweet.getCreatedAt(), Long.valueOf(1593721099));
        Assert.assertEquals(tweet.getText(), "tweet tweet");
        Assert.assertTrue(tweet.getUser() instanceof User);
    }
}