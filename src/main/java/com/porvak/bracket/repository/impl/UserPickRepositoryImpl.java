package com.porvak.bracket.repository.impl;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.porvak.bracket.domain.Team;
import com.porvak.bracket.domain.UserPick;
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
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.*;
import static com.porvak.bracket.domain.BracketConstants.*;
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
//        GameTeam userGamePick = userTournament.findTeamByUserPick(userPick);
//        if(userGamePick.doesUserPickExist()){
//            removeOldPick(userTournament, userPick);
//        }
//        userGamePick.setUserPick(teamByIdMap.get(userPick.getTeamId()));

        //Check if user has made a pick before, if not create the UserPicks object.
//        UserPicks userPicks = userPicksRepository.findByUserIdAndPoolId(userId, poolId);
//        if(userPicks == null || !userPicks.hasUserPicks()){
//            insertUserPicks(userId, poolId, userPick);
//            return;
//        }

//        removeOldPick(userId, poolId, userPick);
        DBObject dbObject = new BasicDBObject();

        Team team = teamByIdMap.get(userPick.getTeamId());
        mongoTemplate.getConverter().write(team, dbObject);

        Query basicQuery = query(where("_id").is(userTournament.getId()));
        int roundId = getDbRoundIdForGame(userPick.getGameId(), userPick.getRegionId());
        String userPickLocation = String.format("regions.%s.rounds.%s.games.%s.teams.%s.userPick", userPick.getRegionId(), roundId,
                getDbGameId(roundId, userPick.getGameId(), userPick.getRegionId()), userPick.getPositionId());

        String previousGameLocation = String.format("regions.%s.rounds.%s.games.%s.teams.%s.userPick", userPick.getRegionId(), roundId,
                getDbGameId(roundId, userPick.getGameId(), userPick.getRegionId()), userPick.getPositionId());

        WriteResult result = mongoTemplate.updateFirst(basicQuery, new Update().set(userPickLocation, dbObject), UserTournament.class);
//        addGameWinnerForPreviousGame(userTournament, userId, poolId, userPick, dbObject);
    }

    private void removeOldPick(UserTournament userTournament, UserPick userPick) {
        DBObject pickToDelete = new BasicDBObject(userPick.getKey());
//TODO: FIX THIS
        Query detailedQuery = query(where("_id").is(userTournament.getId())
                .and("regions.regionId").is(userPick.getRegionId()));
        List<Map> result = mongoTemplate.find(detailedQuery, Map.class);
//                .and("regions.rounds.roundId").is(userPick.get)
//                .and("picks.gameId").is(userPick.getGameId()));
//        mongoTemplate.updateFirst(detailedQuery, new Update().pull("picks", pickToDelete), UserPicks.class);
    }

    @Override
    public void addTieBreaker(String userId, String poolId, int tieBreaker) {
        Query findUserTournament = query(where("userId").is(userId).and("poolId").is(poolId));
        WriteResult result = mongoTemplate.updateFirst(findUserTournament, new Update().set("tieBreaker", tieBreaker), UserTournament.class);
    }

    @Override
    public void removeUserPick(String userId, String poolId, int regionId, int gameId, int position) {
        UserTournament userTournament = checkNotNull(userTournamentRepository.findByUserIdAndPoolId(userId, poolId));
        Query basicQuery = query(where("_id").is(userTournament.getId()));

        int roundId = getDbRoundIdForGame(gameId, regionId);
        String userPickLocation = String.format("regions.%s.rounds.%s.games.%s.teams.%s.userPick", regionId, roundId,
                getDbGameId(roundId, gameId, regionId), position);
        WriteResult result = mongoTemplate.updateFirst(basicQuery, new Update().unset(userPickLocation), UserTournament.class);
    }
}
