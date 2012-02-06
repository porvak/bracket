package com.porvak.bracket.utils.builder;

import com.google.common.collect.Lists;
import com.porvak.bracket.domain.Game;
import com.porvak.bracket.domain.GameStatus;
import com.porvak.bracket.domain.GameTeam;

import java.util.ArrayList;

import static com.porvak.bracket.domain.GameStatus.*;

public class GameBuilder {
    
    private int gameId;
    private GameStatus status;
    private ArrayList<GameTeam> teams;
    
    public GameBuilder(){
        defaults();
    }

    private void defaults(){
        gameId = 1;
        status = FUTURE;
        teams = Lists.newArrayList(null, null);
    }
    
    public GameBuilder withGameId(int gameId){
        this.gameId = gameId;
        return this;
    }
    
    public GameBuilder withStatus(GameStatus status){
        this.status = status;
        return this;
    }
    
    public GameBuilder addTeam(String id, String name, int seed, int position, int score, boolean winner){
        GameTeam team = new GameTeam();
        team.setId(id);
        team.setName(name);
        team.setPosition(position);
        team.setScore(score);
        team.setWinner(winner);
        team.setSeed(seed);
        teams.add(team);
        return this;
    }
    
    public GameBuilder addTeam(GameTeamBuilder gameTeamBuilder){
        teams.add(gameTeamBuilder.build());
        return this;
    }

    public GameBuilder addTeam(GameTeam team){
        teams.add(team);
        return this;
    }

    public Game build(){
        Game game = new Game();
        game.setGameId(gameId);
        if(teams != null && !teams.isEmpty()){
            for (GameTeam team : teams) {
                if(team != null){
                    game.addTeam(team);
                }
            }
        }
        game.setStatus(status);
        return game;
    }

}
