package com.future.tailormade.repository.user;

import com.future.tailormade.model.entity.user.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findByEmail(String email);

    Mono<Boolean> existsByEmail(String email);
}
