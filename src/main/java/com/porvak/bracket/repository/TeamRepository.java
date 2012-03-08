package com.porvak.bracket.repository;

import com.porvak.bracket.domain.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TeamRepository extends MongoRepository<Team, String> {

    List<Team> findBySeedGreaterThan(int seed);
}
