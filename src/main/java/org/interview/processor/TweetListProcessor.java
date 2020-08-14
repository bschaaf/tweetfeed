package org.interview.processor;

import java.time.Instant;
import java.util.List;

import org.interview.model.RunStatistics;
import org.interview.model.RunStatisticsRepository;
import org.interview.model.Tweet;
import org.interview.model.TweetRepository;
import org.interview.report.Reporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

/**
 * Class which performs operations and processes with a list of Tweets for a
 * track. Builder style.
 */
@Log4j2
@Component
public class TweetListProcessor {
    private static boolean stop;
    @Autowired
    private TweetRepository tweetRepository;
    @Autowired
    private RunStatisticsRepository runStatisticsRepository;

    @Autowired
    private Reporter reporter;

    @Value("${twitter.max_tweets}")
    private int MAX_TWEETS;
    @Value("${twitter.max_duration}")
    private int MAX_DURATION;
    private String TRACK;

    public TweetListProcessor configure(String track) {
        this.TRACK = track;
        return this;
    }

    public TweetListProcessor sort(List<Tweet> tweetList) {
        tweetList.sort(Tweet.TweetComparator);
        return this;
    }

    public TweetListProcessor storeAndReportTweetList(List<Tweet> tweetList) {
        saveTweetList(tweetList).subscribe(s -> log.info("TweetList saved."));
        reporter.groupAndPrintTweetReport(tweetList);
        return this;
    }

    protected Mono<List<Tweet>> saveTweetList(List<Tweet> tweetList) {
        return tweetRepository.saveAll(tweetList).collectList();
    }

    public TweetListProcessor storeAndReportRunStatistics(List<Tweet> tweetList) {
        saveRunStatistics(tweetList, Instant.now().getEpochSecond()).subscribe(stats -> {
            reporter.printStatistics(stats);
            reporter.printOverallStatistics(this.TRACK);
            stop = true;
        });
        return this;
    }

    protected Mono<RunStatistics> saveRunStatistics(List<Tweet> tweetList, long timeStamp) {
        return runStatisticsRepository
                .save(new RunStatistics(tweetList, timeStamp, this.TRACK, MAX_TWEETS, MAX_DURATION));
    }

    public void end() {
        while (!stop)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error("Interrupted.");
                System.exit(1);
            }
        System.exit(0);
    }
}