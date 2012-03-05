package com.porvak.bracket.repository.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.porvak.bracket.domain.UserPick;
import com.porvak.bracket.domain.UserPicks;
import com.porvak.bracket.repository.PoolRepository;
import com.porvak.bracket.repository.UserPickRepository;
import com.porvak.bracket.repository.UserPicksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.*;

@Repository
public class UserPickRepositoryImpl implements UserPickRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserPickRepositoryImpl.class);

    @Inject
    private MongoTemplate mongoTemplate;

    @Inject
    private UserPicksRepository userPicksRepository;

    @Inject
    private PoolRepository poolRepository;

    @Override
    public void updateUserPick(String userId, String poolId, UserPick userPick) {
        //Check if user has made a pick before, if not create the UserPicks object.
        UserPicks userPicks = userPicksRepository.findByUserIdAndPoolId(userId, poolId);
        if(userPicks == null || !userPicks.hasUserPicks()){
            insertUserPicks(userId, poolId, userPick);
            return;
        }

        removeOldPick(userId, poolId, userPick);
        addNewUserPick(userId, poolId, userPick);
    }

    private void addNewUserPick(String userId, String poolId, UserPick userPick) {
        DBObject dbObject = new BasicDBObject();
        mongoTemplate.getConverter().write(userPick, dbObject);

        Query basicQuery = query(where("poolId").is(poolId)
                .and("userId").is(userId));
        //TODO: add handling for when data is not added
        WriteResult result = mongoTemplate.updateFirst(basicQuery, new Update().addToSet("picks", dbObject), UserPicks.class);
    }

    private void insertUserPicks(String userId, String poolId, UserPick userPick) {
        UserPicks newUserPicks = new UserPicks();
        newUserPicks.setPoolId(poolId);
        newUserPicks.setUserId(userId);
        //TODO: add handling for no result
        newUserPicks.setTournamentId(poolRepository.findOne(poolId).getTournamentId());
        newUserPicks.addUserPick(userPick);
        userPicksRepository.save(newUserPicks);
    }

    private void removeOldPick(String userId, String poolId, UserPick userPick) {
        DBObject pickToDelete = new BasicDBObject(userPick.getKey());
        Query detailedQuery = query(where("poolId").is(poolId)
                .and("userId").is(userId)
                .and("picks.regionId").is(userPick.getRegionId())
                .and("picks.gameId").is(userPick.getGameId()));
        mongoTemplate.updateFirst(detailedQuery, new Update().pull("picks", pickToDelete), UserPicks.class);
    }
}
