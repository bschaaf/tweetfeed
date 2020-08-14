package org.interview.query;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.interview.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

/**
 * Class which performs operations and processes with a list of Tweets for a
 * track. Builder style.
 */
@Log4j2
@Component
public class TweetListProvider {
    private QueryParams queryParams;

    @Autowired
    private WebClientFactory webClientFactory;
    private WebClient webClient;

    private int tweetCounter = 0;

    private String BASE_URL;
    private int MAX_TWEETS;
    private int MAX_DURATION;

    TweetListProvider(@Value("${twitter.base_url}") String baseUrl, @Value("${twitter.max_tweets}") int maxTweets,
            @Value("${twitter.max_duration}") int maxDuration) {
        this.BASE_URL = baseUrl;
        this.MAX_TWEETS = maxTweets;
        this.MAX_DURATION = maxDuration;
    }

    public TweetListProvider configure(QueryParams queryParams) {
        this.queryParams = queryParams;
        Optional<WebClient> optionalWebClient = this.webClientFactory.create(this.queryParams.getBody());
        optionalWebClient.ifPresentOrElse(value -> this.webClient = value, () -> {
            log.info("WebClient unavailable");
            System.exit(1);
        });
        return this;
    }

    public Mono<List<Tweet>> provide() {
        return runQuery();
    }

    private Mono<List<Tweet>> runQuery() {
        return this.webClient.post().uri(BASE_URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(this.queryParams.getBody())).retrieve().bodyToFlux(Tweet.class)
                .doOnEach(t -> System.out.printf("%d ", ++tweetCounter))
                .bufferTimeout(MAX_TWEETS, Duration.ofSeconds(MAX_DURATION)).take(1).single();
    }

}