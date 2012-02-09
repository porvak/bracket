package com.porvak.bracket.repository;

import com.porvak.bracket.domain.UserPicks;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPicksRepository extends MongoRepository<UserPicks, String> {
}
