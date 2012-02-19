package com.porvak.bracket.repository;

import com.porvak.bracket.domain.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, String> {
}
