package org.interview.model;


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
public class TweetRepositoryTest {

    @Autowired 
    TweetRepository tweetRepository;

    @Test
    public void saveTweetToRepositoryAndCompareWithReturned() {
        Tweet tweet = new Tweet("1","Thu Jul 02 20:18:19 +0000 2020","tweet tweet", 
                        new User("10", "Mon Feb 03 10:11:12 +0000 2020", "",""));
        tweetRepository
            .save(tweet)
            .subscribe(saved -> Assert.assertEquals(saved, tweet));
    }
    
}