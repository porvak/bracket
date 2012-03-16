package com.porvak.bracket.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Round extends AbstractBracket implements Comparable<Round>{
    private int roundId;


    private int dbRoundId;
    private String roundName;
    @JsonIgnore
    @Field("games")
    private Map<Integer, Game> gameMap;

    public Round(){
        gameMap = Maps.newHashMap();
    }

    public Game findGameById(int id){
        return gameMap.get(id);
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public int getDbRoundId() {
        return dbRoundId;
    }

    public void setDbRoundId(int dbRoundId) {
        this.dbRoundId = dbRoundId;
    }

    public List<Game> getGames() {
        List<Game> gamesList = Lists.newLinkedList(gameMap.values());
        Collections.sort(gamesList);
        return gamesList;
    }

    public void addGame(Game game){
        gameMap.put(game.getGameId(), game);
    }

    @Override
    public int compareTo(Round o) {
        if(roundId > o.getRoundId())
            return 1;
        else if(roundId < o.getRoundId())
            return -1;
        else
            return 0;
    }
}
