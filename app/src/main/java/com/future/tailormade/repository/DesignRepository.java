package com.future.tailormade.repository;

import com.future.tailormade.model.entity.design.Design;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DesignRepository extends ReactiveMongoRepository<Design, String> {

    Mono<Integer> countAllByTailorId(String tailorId);

    Mono<Integer> countAllByTitleIsLikeOrCategoryExists(String keyword);

    @Query("{ id: { $exists: true }}")
    Flux<Design> findAllByTailorId(String tailorId, Pageable pageable);

    @Query("{ id: { $exists: true }}")
    Flux<Design> findAllByTitleIsLikeOrCategoryExists(String keyword, Pageable pageable);

    Mono<Design> findByTailorIdAndId(String tailorId, String id);

    Mono<Void> deleteByTailorIdAndId(String tailorId, String id);
}
