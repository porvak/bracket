package com.porvak.bracket.repository;

import com.porvak.bracket.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>{
}
