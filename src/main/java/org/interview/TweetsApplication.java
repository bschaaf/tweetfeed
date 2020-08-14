package org.interview;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import org.interview.processor.TweetListProcessor;
import org.interview.query.QueryParams;
import org.interview.query.TweetListProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

import lombok.extern.log4j.Log4j2;

/**
 * Spring commandline application to query tweets for a specified track.
 * The resulting tweet list is stored in a Mongdo database and subsequently
 * processed for reporting. Statistics are calculated for each run and 
 * overall for a track.
 */
@SpringBootApplication(proxyBeanMethods = false)
@Log4j2
public class TweetsApplication extends AbstractReactiveMongoConfiguration{
	@Autowired
	private TweetListProvider tweetListProvider;
	@Autowired
	private TweetListProcessor tweetListProcessor;

	private final String MONGO_DB;
	private final String MONGO_URI;

	TweetsApplication(@Value("${mongo.db}") String mongoDb, @Value("${mongo.uri}") String mongoUri){
		this.MONGO_DB=mongoDb;
		this.MONGO_URI=mongoUri;
    }

 	@Bean
	public MongoClient reactiveMongoClient() {
		return MongoClients.create(MONGO_URI);
	}

	@Override
	protected String getDatabaseName() {
		return MONGO_DB;
	}

	public static void main(String[] args) {
		SpringApplication.run(TweetsApplication.class, args);
	}

	// First the tweet list is obtained from Twitter and subsequently processed.
	@Profile("!test")
	@Bean
	public CommandLineRunner queryTweetFeed() {
		return args -> {
			var queryParams = new QueryParams(args);
		    tweetListProvider
				.configure(queryParams)
				.provide()
				.subscribe(tweetList -> {
					if (tweetList.isEmpty()) {
						log.info("No tweets!");
						System.exit(0);
					} else {
						System.out.println();
						tweetListProcessor
							.configure(queryParams.getTrack())
							.sort(tweetList)
							.storeAndReportTweetList(tweetList)
							.storeAndReportRunStatistics(tweetList)
							.end();
					}
				});	
		};
	}
}
