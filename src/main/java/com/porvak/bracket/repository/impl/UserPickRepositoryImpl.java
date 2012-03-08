package com.porvak.bracket.repository.impl;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.porvak.bracket.domain.GameTeam;
import com.porvak.bracket.domain.Team;
import com.porvak.bracket.domain.UserPick;
import com.porvak.bracket.domain.UserPicks;
import com.porvak.bracket.domain.user.UserTournament;
import com.porvak.bracket.repository.PoolRepository;
import com.porvak.bracket.repository.TeamRepository;
import com.porvak.bracket.repository.UserPickRepository;
import com.porvak.bracket.repository.UserPicksRepository;
import com.porvak.bracket.repository.UserTournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Map;

import static com.google.common.base.Preconditions.*;
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
    
    @Inject
    private UserTournamentRepository userTournamentRepository;

    @Inject
    private TeamRepository teamRepository;
    
    private Map<String, Team> teamByIdMap;

    @PostConstruct
    public void init(){

        teamByIdMap = Maps.uniqueIndex(teamRepository.findBySeedGreaterThan(0), new Function<Team, String>() {
                @Override
                public String apply(@Nullable Team input) {
                    if (input == null) {
                        return null;
                    }
                    return input.getId();
                }
            });

        checkNotNull(teamByIdMap);
    }

    @Override
    public void updateUserPick(String userId, String poolId, UserPick userPick) {
        //Pull users tournament record
        UserTournament userTournament = checkNotNull(userTournamentRepository.findByUserIdAndPoolId(userId, poolId));
        
        //Find game that user has picked
        GameTeam userGamePick = userTournament.findTeamByUserPick(userPick);
        if(userGamePick.doesUserPickExist()){
            removeOldPick(userTournament.getId(), userPick);
        }
        userGamePick.setUserPick(teamByIdMap.get(userPick.getTeamId()));

        //Check if user has made a pick before, if not create the UserPicks object.
//        UserPicks userPicks = userPicksRepository.findByUserIdAndPoolId(userId, poolId);
//        if(userPicks == null || !userPicks.hasUserPicks()){
//            insertUserPicks(userId, poolId, userPick);
//            return;
//        }

//        removeOldPick(userId, poolId, userPick);
        addNewUserPick(userId, poolId, userPick);
    }

    @Override
    public void addTieBreaker(String userId, String poolId, int tieBreaker) {
        Query findUserTournament = query(where("userId").is(userId).and("poolId").is(poolId));
        WriteResult result = mongoTemplate.updateFirst(findUserTournament, new Update().set("tieBreaker", tieBreaker), UserTournament.class);
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

    private void removeOldPick(String userTournamentId, UserPick userPick) {
        DBObject pickToDelete = new BasicDBObject(userPick.getKey());
//TODO: FIX THIS
//        Query detailedQuery = query(where("_id").is(userTournamentId)
//                .and("regioins.regionId").is(userPick.getRegionId())
//                .and()
//                .and("picks.gameId").is(userPick.getGameId()));
//        mongoTemplate.updateFirst(detailedQuery, new Update().pull("picks", pickToDelete), UserPicks.class);
    }
}
