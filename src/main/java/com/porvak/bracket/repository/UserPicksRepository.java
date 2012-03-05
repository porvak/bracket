package com.porvak.bracket.repository;

import com.porvak.bracket.domain.UserPicks;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserPicksRepository extends MongoRepository<UserPicks, String> {

    /**
     * Finds a single UserPicks dataset based off of the supplied userId;
     *
     * @param userId
     * @return
     */
    UserPicks findByUserId(String userId);

    /**
     * Finds a single UserPicks dataset based off the supplied
     * userId and poolId
     *
     * @param userId
     * @param poolId
     * @return
     */
    UserPicks findByUserIdAndPoolId(String userId, String poolId);
}
