package com.porvak.bracket.domain;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.springframework.data.annotation.Transient;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class Round extends AbstractBracket {
    private int roundId;
    private String roundName;
    private List<Game> games;
    
    @Transient
    private Map<Integer, Game> indexedGames;
    
    public Game findGameById(int id){
        if(indexedGames == null && games != null){
            indexedGames = Maps.uniqueIndex(games, new Function<Game, Integer>() {
                @Override
                public Integer apply(@Nullable Game input) {
                    if(input == null){
                        return null;
                    }
                    return input.getGameId();
                }
            });
        }

        if(indexedGames == null){
            return null;
        }

        return indexedGames.get(id);
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
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;

        // reset index
        indexedGames = null;
    }
}
