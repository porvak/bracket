package com.porvak.bracket.repository;

import com.porvak.bracket.domain.Tournament;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TournamentRepository extends MongoRepository<Tournament, String> {
}
