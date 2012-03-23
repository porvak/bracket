package com.porvak.bracket.repository;

import com.porvak.bracket.domain.Leaderboard;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LeaderboardRepository extends MongoRepository<Leaderboard, String> {
    
    List<Leaderboard> findByPoolId(String poolId, Sort sort);
    
    Leaderboard findByPoolIdAndUserId(String poolId, String userId);
}
