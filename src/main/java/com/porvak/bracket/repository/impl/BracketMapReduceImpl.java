package com.porvak.bracket.repository.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import com.porvak.bracket.domain.Leaderboard;
import com.porvak.bracket.domain.LeaderboardMapReduce;
import com.porvak.bracket.domain.User;
import com.porvak.bracket.repository.LeaderboardRepository;
import com.porvak.bracket.repository.UserRepository;
import org.bson.types.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.porvak.bracket.domain.BracketConstants.*;
import static org.springframework.data.domain.Sort.Direction.*;

@Repository
public class BracketMapReduceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(BracketMapReduceImpl.class);

    @Inject
    MongoTemplate mongoTemplate;
    
    @Inject
    UserRepository userRepository;

    @Inject
    LeaderboardRepository leaderboardRepository;

    public void generateLeaderboard(){
        MapReduceOptions mapReduceOptions = MapReduceOptions.options();
        Map<String, Object> scopeVariables = Maps.newLinkedHashMap();
        try {
            scopeVariables.put("pointsInRound", new Code(Files.toString(new ClassPathResource("mapreduce/pointsInRound.js").getFile(), Charset.defaultCharset())));
            scopeVariables.put("maxPointsInRound", new Code(Files.toString(new ClassPathResource("mapreduce/maxPointsInRound.js").getFile(), Charset.defaultCharset())));
            mapReduceOptions.finalizeFunction(Files.toString(new ClassPathResource("mapreduce/finalize.js").getFile(), Charset.defaultCharset()));
        }
        catch (IOException e) {
            LOGGER.error("Error reading map/reduce files", e);
            throw new RuntimeException("Error reading map/reduce files", e);
        }

        mapReduceOptions.scopeVariables(scopeVariables);
        mapReduceOptions.outputCollection("leaderboardMapReduce");
        mapReduceOptions.outputTypeReplace();
        mapReduceOptions.verbose(true);

        MapReduceResults<LeaderboardMapReduce> result = mongoTemplate.mapReduce("userTournament", "classpath:/mapreduce/map.js", "classpath:/mapreduce/reduce.js", mapReduceOptions, LeaderboardMapReduce.class);
        LOGGER.info("Leaderboard generation complete. Timing: [{}] Counts: [{}]", result.getTiming(), result.getCounts());

        Map<String, User> userMapById = Maps.uniqueIndex(userRepository.findAll(), new Function<User, String>() {
            @Override
            public String apply(@Nullable User input) {
                if(input == null){
                    return null;
                }
                return input.getId();
            }
        });

        Multimap<LeaderboardMrKey, LeaderboardMapReduce> multimap = ArrayListMultimap.create();

        List<LeaderboardMrKey> keys = Lists.newArrayList();
        for (LeaderboardMapReduce leaderboardMapReduce : result) {
            LeaderboardMrKey key = new LeaderboardMrKey(leaderboardMapReduce.getId().getUserId(), leaderboardMapReduce.getId().getPoolId());
            Collection<LeaderboardMrKey> filteredKeys = Collections2.filter(keys, Predicates.equalTo(key));
            if(CollectionUtils.hasUniqueObject(filteredKeys)){
                key = filteredKeys.iterator().next();
            }
            else {
                keys.add(key);
            }

            multimap.put(key, leaderboardMapReduce);
        }

        Map<LeaderboardMrKey, Collection<LeaderboardMapReduce>> leaderboardMap = multimap.asMap();
        leaderboardRepository.deleteAll();

        for (Map.Entry<LeaderboardMrKey, Collection<LeaderboardMapReduce>> entry : leaderboardMap.entrySet()) {
            Leaderboard leaderboard = new Leaderboard();
            User currentUser = userMapById.get(entry.getKey().getUserId());

            leaderboard.setUserId(entry.getKey().getUserId());
            leaderboard.setPoolId(entry.getKey().getPoolId());
            leaderboard.setUsername(currentUser.getTwitterName());
            for (LeaderboardMapReduce resultsByRound : entry.getValue()) {
                leaderboard.addRoundScore(resultsByRound.getId().getRoundId(), resultsByRound.getValue().getScore(), resultsByRound.getValue().getAvailable());
            }

            leaderboardRepository.save(leaderboard);
        }
        
        //-- Add Rankings
        List<Leaderboard> orderedLeaderboard = leaderboardRepository.findByPoolId(POOL_ID, new Sort(DESC, "totalScore"));

        int place = 0;
        int previousScore = 1000;
        int totalParticipants = orderedLeaderboard.size();
        int sameScoreCount = 1;

        for (Leaderboard leaderboard : orderedLeaderboard) {
            if(leaderboard.getTotalScore() < previousScore){
                previousScore = leaderboard.getTotalScore();
                place = place + sameScoreCount;
                sameScoreCount = 0;
            }

            leaderboard.setPlace(place);
            leaderboard.setTotalParticipants(totalParticipants);
            sameScoreCount = sameScoreCount + 1;
            leaderboardRepository.save(leaderboard);
        }

    }
    
    private class LeaderboardMrKey{
        private final String poolId;
        private final String userId;
        private final Predicate<Object> instance = Predicates.instanceOf(LeaderboardMrKey.class);

        private LeaderboardMrKey(final String userId, final String poolId) {
            this.userId = userId;
            this.poolId = poolId;
        }

        public String getPoolId() {
            return poolId;
        }

        public String getUserId() {
            return userId;
        }

        @Override
        public boolean equals(Object o) {
            if(o == null || !instance.apply(o))
                return false;
            LeaderboardMrKey obj = (LeaderboardMrKey) o;
            return this.getPoolId().equals(obj.getPoolId()) && this.getUserId().equals(obj.getUserId());

        }
    }

}
