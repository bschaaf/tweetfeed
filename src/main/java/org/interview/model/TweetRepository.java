package org.interview.model;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface TweetRepository extends ReactiveMongoRepository<Tweet,String> {
}