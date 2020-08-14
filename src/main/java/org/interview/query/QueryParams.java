package org.interview.query;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * Class to represent to parameters for the query, especially which track is to be queried.
 * Contains MultiValueMap to be used to generate the body of the request.
 * @param args commandline parameters
 */ 
@Log4j2
@Data
public class QueryParams {
    private String track="bieber";
    private MultiValueMap<String, String> body= new LinkedMultiValueMap<>();

    public QueryParams(String... args){
		if (args.length > 0) {
			log.info("Using arguments as tracks");
			this.track = String.join(",", args);
		}
        body.add("track", track);
		log.info("Filtering tracks [{}]", track);
	} 
}