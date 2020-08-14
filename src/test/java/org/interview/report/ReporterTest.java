package org.interview.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.interview.model.Tweet;
import org.interview.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class ReporterTest {
    @Autowired
    Reporter reporter;

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
    public void groupTweetsByUserAndCheckOrderTest(){
        Map<User, List<Tweet>> map = reporter.groupTweetsByUser(tweetList);
        
        // Group by user
        map.keySet().stream().forEach(user -> 
            map.get(user).forEach(tweet ->
            Assert.assertEquals(user, tweet.getUser()))
        );

        // Time order tweets per list of each user
        map.values().forEach(list ->{
            var length = list.size();
            if( length > 1 )
                for(var i=0; i< length -1; i++) 
                    Assert.assertTrue(
                        list.get(i).getCreatedAt() <= list.get(i+1).getCreatedAt());
            });       
    }

    @Test
    public void getGroupedTweetListJsonTest(){
        Map<User, List<Tweet>> map = reporter.groupTweetsByUser(tweetList);
        String report = reporter.getGroupedAndSortedTweetListJson(map);
        Assert.assertEquals(report,
                "{\"id_str\":\"6\",\"created_at\":\"Thu Jul 02 20:18:39 +0000 2020\",\"text\":\"tweet tweet\",\"user\":{\"id_str\":\"12\",\"created_at\":\"Thu Sep 12 15:03:19 +0000 2019\",\"name\":\"name3\",\"screen_name\":\"screenname3\"}}\n" +
                "{\"id_str\":\"2\",\"created_at\":\"Thu Jul 02 20:18:22 +0000 2020\",\"text\":\"twit twat\",\"user\":{\"id_str\":\"10\",\"created_at\":\"Fri Jan 03 10:14:18 +0000 2020\",\"name\":\"name1\",\"screen_name\":\"screenname1\"}}\n" +
                "{\"id_str\":\"3\",\"created_at\":\"Thu Jul 02 20:18:29 +0000 2020\",\"text\":\"twat twut\",\"user\":{\"id_str\":\"10\",\"created_at\":\"Fri Jan 03 10:14:18 +0000 2020\",\"name\":\"name1\",\"screen_name\":\"screenname1\"}}\n" +
                "{\"id_str\":\"5\",\"created_at\":\"Thu Jul 02 20:18:32 +0000 2020\",\"text\":\"twot tweet\",\"user\":{\"id_str\":\"10\",\"created_at\":\"Fri Jan 03 10:14:18 +0000 2020\",\"name\":\"name1\",\"screen_name\":\"screenname1\"}}\n" +
                "{\"id_str\":\"1\",\"created_at\":\"Thu Jul 02 20:18:19 +0000 2020\",\"text\":\"tweet twit\",\"user\":{\"id_str\":\"11\",\"created_at\":\"Thu Jul 02 20:18:19 +0000 2020\",\"name\":\"name2\",\"screen_name\":\"screenname2\"}}\n" +
                "{\"id_str\":\"4\",\"created_at\":\"Thu Jul 02 20:18:31 +0000 2020\",\"text\":\"twat twot\",\"user\":{\"id_str\":\"11\",\"created_at\":\"Thu Jul 02 20:18:19 +0000 2020\",\"name\":\"name2\",\"screen_name\":\"screenname2\"}}\n"                
            );
    }

}