package com.porvak.bracket.repository;

import com.porvak.bracket.domain.UserPicks;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserPicksRepository extends MongoRepository<UserPicks, String> {
    
    UserPicks findByUserId(String userId);
}
