package com.porvak.bracket.domain;

import com.google.common.collect.ImmutableSortedSet;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.SortedMap;

public class Round extends AbstractBracket implements Comparable<Round>{
    private int roundId;
    private String roundName;

//    @Transient
//    private List<Game> games;
    
    @JsonIgnore
    @Field("games")
    private SortedMap<Integer, Game> gameMap;

    public Round(){}
    
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

    public List<Game> getGames() {
        return ImmutableSortedSet.copyOf(gameMap.values()).asList();
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
