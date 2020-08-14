# Java Exercise #

Good day,

For this implementation I have used Reactive Spring Boot and Mongo DB. The authentication with HttpRequestFactory has been modified to be used with Spring WebClient.

To run the application a Mongo DB server listening on the standard port and a database (mongdo.db in application.properties) needs to be created. The output is written to the standard output. However it is also possible to specify an output file in application.properties.

The following output files have been uploaded to the repository:
+ standard.out (representing the screen output of the process)
+ java-exercise-1593811536.json (json output for the specified file)

## Configuration ##

In application.properties the following properties can be configured:

+ twitter.consumer_key=<consumer key>
+ twitter.consumer_secret=<consumer secret>
+ twitter.base_url=https://stream.twitter.com/1.1/statuses/filter.json
+ twitter.max_tweets=100 (maximum number of tweets during one run)
+ twitter.max_duration=30 (max duration of one run)
+ report.out_file=java-exercise-%t.json (output file, %t is replaced by a timestamp)
+ mongo.db=tweetdb (Mongo database)

## Execution ##

The application can be run with `mvn spring-boot:run`. In that case the track to be queried is 'bieber'.

It is also possible to run the application as a jar. In case arguments are given, they supersede 'bieber'and form a new track.

The output also gives some statistics information about the average tweet of the last run and the overall runs. This output only appears on the standard out, at the end.

For any clarification: benoit.schaaf@yahoo.com, +31625051061.

Best regards,
Benoît




