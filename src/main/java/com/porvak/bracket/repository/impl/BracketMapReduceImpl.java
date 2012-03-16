package com.porvak.bracket.repository.impl;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.porvak.bracket.domain.Leaderboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

@Repository
public class BracketMapReduceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(BracketMapReduceImpl.class);

    @Inject
    MongoTemplate mongoTemplate;

    public void generateLeaderboard(){
        MapReduceOptions mapReduceOptions = new MapReduceOptions();
        Map<String, Object> scopeVariables = Maps.newHashMap();
        try {
            scopeVariables.put("pointsInRound", Files.toString(new ClassPathResource("mapreduce/pointsInRound.js").getFile(), Charset.defaultCharset()));
            scopeVariables.put("maxPointsInRound", Files.toString(new ClassPathResource("mapreduce/maxPointsInRound.js").getFile(), Charset.defaultCharset()));
            mapReduceOptions.finalizeFunction(Files.toString(new ClassPathResource("mapreduce/finalize.js").getFile(), Charset.defaultCharset()));
        }
        catch (IOException e) {
            LOGGER.error("Error reading map/reduce files", e);
            throw new RuntimeException("Error reading map/reduce files", e);
        }

        mapReduceOptions.scopeVariables(scopeVariables);
        mapReduceOptions.outputCollection("leaderboard");

        mongoTemplate.mapReduce("userTournament", "classpath:/mapreduce/map.js", "classpath:/mapreduce/reduce.js", mapReduceOptions, Leaderboard.class);
    }
}
