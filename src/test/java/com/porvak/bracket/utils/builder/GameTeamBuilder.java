package com.porvak.bracket.utils.builder;

import com.porvak.bracket.domain.GameTeam;

public class GameTeamBuilder {

    private String id;
    private String name;
    private int position;
    private int score;
    private boolean winner;
    private int seed;
    
    public GameTeamBuilder(){
        defaults();
    }

    private void defaults() {
        id = "1";
        name = "Purdue";
        position = 0;
        score = 0;
        winner = false;
        seed = 1;
    }
    
    public GameTeamBuilder withId(String id){
        this.id = id;
        return this;
    }
    
    public GameTeamBuilder withName(String name){
        this.name = name;
        return this;
    }

    public GameTeamBuilder withPosition(int position){
        this.position = position;
        return this;
    }

    public GameTeamBuilder withScore(int score){
        this.score = score;
        return this;
    }

    public GameTeamBuilder withWinner(boolean winner){
        this.winner = winner;
        return this;
    }

    public GameTeamBuilder withSeed(int seed){
        this.seed = seed;
        return this;
    }
    
    public GameTeam build(){
        GameTeam team = new GameTeam();
        team.setId(id);
        team.setName(name);
        team.setPosition(position);
        team.setScore(score);
        team.setWinner(winner);
        team.setSeed(seed);
        return team;
    }
}
