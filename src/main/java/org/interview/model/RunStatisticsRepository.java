package org.interview.model;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;



public interface RunStatisticsRepository extends ReactiveMongoRepository<RunStatistics,String> {
    // Return all RunStatistics for a specific track
    Flux<RunStatistics> findAllByTrack(String track);
}