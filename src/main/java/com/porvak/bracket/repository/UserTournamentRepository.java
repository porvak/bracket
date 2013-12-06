package com.porvak.bracket.repository;

import com.porvak.bracket.domain.user.UserTournament;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserTournamentRepository extends MongoRepository<UserTournament, String>{
    
    UserTournament findByUserIdAndPoolId(String userId, String poolId);
}
