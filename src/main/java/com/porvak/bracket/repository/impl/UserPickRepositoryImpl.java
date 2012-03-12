package com.porvak.bracket.repository.impl;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.porvak.bracket.domain.GamePointer;
import com.porvak.bracket.domain.Team;
import com.porvak.bracket.domain.UserPick;
import com.porvak.bracket.domain.user.UserTournament;
import com.porvak.bracket.repository.TeamRepository;
import com.porvak.bracket.repository.UserPickRepository;
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
import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.*;
import static com.porvak.bracket.domain.BracketConstants.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.*;

@Repository
public class UserPickRepositoryImpl implements UserPickRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserPickRepositoryImpl.class);
    public static final String USER_PICK_LOCATION = "regions.%s.rounds.%s.games.%s.teams.%s.userPick";
    public static final String PREVIOUS_GAME_LOCATION = "regions.%s.rounds.%s.games.%s.teams.%s.previousGame.position";
    public static final String USER_PICKED_GAME_WINNER_LOCATION = "regions.%s.rounds.%s.games.%s.userPickedGameWinner";

    @Inject
    private MongoTemplate mongoTemplate;

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
        
        Team team = teamByIdMap.get(userPick.getTeamId());
        DBObject dbObject = new BasicDBObject();
        mongoTemplate.getConverter().write(team, dbObject);

        Query basicQuery = query(where("_id").is(userTournament.getId()));
        int roundId = getDbRoundIdForGame(userPick.getGameId(), userPick.getRegionId());
        String userPickLocation = String.format(USER_PICK_LOCATION, userPick.getRegionId(), roundId,
                getDbGameId(roundId, userPick.getGameId(), userPick.getRegionId()), userPick.getPositionId());
        WriteResult result = mongoTemplate.updateFirst(basicQuery, new Update().set(userPickLocation, dbObject), UserTournament.class);
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

        int dbRoundId = getDbRoundIdForGame(gameId, regionId);
        int dbGameId = getDbGameId(dbRoundId, gameId, regionId);
        String userPickLocation = String.format(USER_PICK_LOCATION, regionId, dbRoundId, dbGameId, position);
        LOGGER.debug("Removing userPickLocation [{}]", userPickLocation);

        WriteResult result = mongoTemplate.updateFirst(basicQuery, new Update().unset(userPickLocation), UserTournament.class);

        // Also, remove corresponding userPickedGameWinner
        GamePointer previousGame = userTournament.getRegionMap().get(regionId).getRoundById(dbRoundId).findGameById(dbGameId).getTeamByPosition(position).getPreviousGame();

        int previousDbRoundId = getDbRoundIdForGame(previousGame.getGameId(), previousGame.getRegionId());
        int previousDbGameId = getDbGameId(previousDbRoundId, previousGame.getGameId(), previousGame.getRegionId());

        String userPickGameWinnerLocation = String.format(USER_PICKED_GAME_WINNER_LOCATION, previousGame.getRegionId(), previousDbRoundId, previousDbGameId);
        LOGGER.debug("Removing userPickedGameWinnerLocation [{}]", userPickGameWinnerLocation);
        result = mongoTemplate.updateFirst(basicQuery, new Update().unset(userPickGameWinnerLocation), UserTournament.class);
    }

    @Override
    public void updatePreviousGame(String userId, String poolId, Map<String, Object> userPickMap) {
        UserPick userPick = new UserPick(userPickMap);
        LinkedHashMap previousGameMap = (LinkedHashMap<String, Object>) userPickMap.get("previousGame");
        int previousGamePosition = Integer.valueOf(checkNotNull(previousGameMap.get("position")).toString());

        int roundId = getDbRoundIdForGame(userPick.getGameId(), userPick.getRegionId());
        String previousGameLocation = String.format(PREVIOUS_GAME_LOCATION, userPick.getRegionId(), roundId,
                getDbGameId(roundId, userPick.getGameId(), userPick.getRegionId()), userPick.getPositionId());

        UserTournament userTournament = checkNotNull(userTournamentRepository.findByUserIdAndPoolId(userId, poolId));
        Query basicQuery = query(where("_id").is(userTournament.getId()));
        WriteResult result = mongoTemplate.updateFirst(basicQuery, new Update().set(previousGameLocation, previousGamePosition), UserTournament.class);
    }

    @Override
    public void addGameWinner(String userId, String poolId, Map<String, Object> userPickMap) {
        UserTournament userTournament = checkNotNull(userTournamentRepository.findByUserIdAndPoolId(userId, poolId));
        int regionId = Integer.valueOf(checkNotNull(userPickMap.get("regionId")).toString());
        int gameId = Integer.valueOf(checkNotNull(userPickMap.get("gameId")).toString());
        int position = Integer.valueOf(checkNotNull(userPickMap.get("position")).toString());
        int dbRoundId = getDbRoundIdForGame(gameId, regionId);
        int dbGameId = getDbGameId(dbRoundId, gameId, regionId);
        GamePointer previousGame = userTournament.getRegionMap().get(regionId).getRoundById(dbRoundId).findGameById(dbGameId).getTeamByPosition(position).getPreviousGame();

        int previousDbRoundId = getDbRoundIdForGame(previousGame.getGameId(), previousGame.getRegionId());
        int previousDbGameId = getDbGameId(previousDbRoundId, previousGame.getGameId(), previousGame.getRegionId());

        String userPickGameWinnerLocation = String.format(USER_PICKED_GAME_WINNER_LOCATION, previousGame.getRegionId(), previousDbRoundId, previousDbGameId);

        Team team = teamByIdMap.get(new UserPick(userPickMap).getTeamId());
        DBObject dbObject = new BasicDBObject();
        mongoTemplate.getConverter().write(team, dbObject);

        Query basicQuery = query(where("_id").is(userTournament.getId()));
        LOGGER.debug("Going to add [{}] to [{}] with region: [{}], gameId: [{}] and previousGame: [{}]", new Object[]{dbObject.toString(), userPickGameWinnerLocation, regionId, gameId, poolId, previousGame});
        WriteResult result = mongoTemplate.updateFirst(basicQuery, new Update().set(userPickGameWinnerLocation, dbObject), UserTournament.class);
    }
}
