package org.interview.report;

import static java.util.stream.Collectors.averagingDouble;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.interview.model.RunStatistics;
import org.interview.model.RunStatisticsRepository;
import org.interview.model.Tweet;
import org.interview.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

/**
 * Contains methods to group, sort and output search results
 */
@Log4j2
@Component
public class Reporter {
	@Autowired
	private RunStatisticsRepository runStatisticsRepository;
	@Value("${report.out_file}")
	private String OUT_FILE;

	public void groupAndPrintTweetReport(List<Tweet> tweetList) {
		printTweetList(groupTweetsByUser(tweetList));
	}

	private void printTweetList(Map<User, List<Tweet>> map) {
		PrintWriter out = new PrintWriter(System.out, true);
		boolean toFile = OUT_FILE.length() > 0;
		if (toFile) out = openFilePrintWriter(OUT_FILE);
		out.println(getGroupedAndSortedTweetListJson(map));
		if (toFile) out.close();
	}

	private PrintWriter openFilePrintWriter(String OUT_FILE) {
		try {
			var fileWriter = new FileWriter(OUT_FILE.replace("%t", Long.toString(Instant.now().getEpochSecond())));
			return new PrintWriter(fileWriter);
		} catch (IOException e) {
			log.error("Can't write to file: " + e.getMessage());
			System.exit(1);
		}
		return null;
	}

	protected String getGroupedAndSortedTweetListJson(Map<User, List<Tweet>> map) {
		var sb = new StringBuilder();
		usersSortedByCreatedAt(map).forEach(user -> {
			map.get(user).forEach(tweet -> sb.append(String.format("%s\n", tweet.jsonString())));
		});
		return sb.toString();
	}

	// Sorts users by createdAt
	protected List<User> usersSortedByCreatedAt(Map<User, List<Tweet>> map) {
		var users = new ArrayList<>(map.keySet());
		users.sort(User.CreatedAtComparator);
		return users;
	}

	// Groups tweets by user
	protected Map<User, List<Tweet>> groupTweetsByUser(List<Tweet> tweetList) {
		return tweetList.stream()
				.collect(groupingBy(Tweet::getUser, collectingAndThen(toCollection(ArrayList::new), list -> {
					list.sort(Tweet.CreatedAtComparator);
					return list;
				})));
	}

	public void printStatistics(RunStatistics runStatistics) {
		System.out.println("\n#################################### STATISTICS ########################################");
		System.out.println(runStatistics);
	}

	public void printOverallStatistics(String track) {
		runStatisticsRepository.findAllByTrack(track).buffer().subscribe(list -> {
			var overallAverage = list.stream().collect(averagingDouble(RunStatistics::getAverage));
			System.out.println(getStatisticsString(track, list.size(), overallAverage));
		});
	}

	private String getStatisticsString(String track, int runs, double overallAverage) {
		return String.format("Overall statistics for track: %s, number of runs: %d, average tweet per second: %.2f\n",
				track, runs, overallAverage);
	}

}