package com.porvak.bracket.repository;

import com.porvak.bracket.domain.Pool;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoolRepository extends MongoRepository<Pool, String>{
}
