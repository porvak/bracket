package com.porvak.bracket.domain;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
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
    
    public Game findByGameId(int id){
        Optional<Game> result = Iterators.tryFind(games.iterator(), new GameByIdPredicate(id));
        if(result.isPresent()){
            return result.get();
        }
        else{
            return null;
        }
    }

    private static class GameByIdPredicate implements Predicate<Game>{
        private final int gameId;
        private GameByIdPredicate(final int gameId){
            this.gameId = gameId;
        }
        
        @Override
        public boolean apply(@Nullable Game input) {
            return input != null && gameId == input.getGameId();
        }
    }
    
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
    
//    @Transient
//    public void setGame(Game game){
//
//    }

    public void setGames(List<Game> games) {
        this.games = games;

        // reset index
        indexedGames = null;
    }
}
