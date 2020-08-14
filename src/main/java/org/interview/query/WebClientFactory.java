package org.interview.query;

import java.util.Optional;

import com.google.api.client.http.GenericUrl;

import org.interview.oauth.twitter.TwitterAuthenticationException;
import org.interview.oauth.twitter.TwitterAuthenticator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.log4j.Log4j2;

/**
 * Factory generating WebClient for a specific request, using OAuth.
 */
@Log4j2
@Component
public class WebClientFactory {   
    @Value("${twitter.consumer_key}")
    private String CONSUMER_KEY;
    @Value("${twitter.consumer_secret}")
    private String CONSUMER_SECRET;
    @Value("${twitter.base_url}")
    private String BASE_URL;

    public Optional<WebClient> create(MultiValueMap<String, String> body) {
        WebClient webClient = null;
        try {
            final var httpHeaders = 
                new TwitterAuthenticator(System.out, CONSUMER_KEY, CONSUMER_SECRET)
                    .getAuthorizationHeaderForRequest(RequestMethod.POST,
                            new GenericUrl(BASE_URL + "?track=" + body.getFirst("track")));

            webClient = 
                WebClient.builder().filter((currentRequest, next) -> {
                    return next.exchange(
                        ClientRequest.from(currentRequest).header(HttpHeaders.AUTHORIZATION, httpHeaders).build());
                    }).build();
        } catch (TwitterAuthenticationException e) {
            log.error("Can't authenticate: " + e.getStackTrace());
        }
        return Optional.ofNullable(webClient);
    }
}