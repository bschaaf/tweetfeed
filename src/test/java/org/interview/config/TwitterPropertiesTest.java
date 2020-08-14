package org.interview.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import  org.junit.Assert;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TwitterPropertiesTest {
 
    @Autowired
    private TwitterProperties twitterProperties;
 
    @Test
    public void whenSimplePropertyQueriedThenReturnsPropertyValue() 
      throws Exception {
        Assert.assertEquals("Incorrectly bound ConsumerKey property", 
          "consumer_key", twitterProperties.getConsumer_key());
        Assert.assertEquals("Incorrectly bound ConsumerSecret property", 
          "consumer_secret", twitterProperties.getConsumer_secret());
        Assert.assertEquals("Incorrectly bound BaseUrl property", 
          "https://stream.twitter.com/1.1/statuses/filter.json", twitterProperties.getBase_url());
        Assert.assertEquals("Incorrectly bound MaxTweets property", 
          100, twitterProperties.getMax_tweets());
        Assert.assertEquals("Incorrectly bound MaxDuration property", 
          30, twitterProperties.getMax_duration());
    }
    
}