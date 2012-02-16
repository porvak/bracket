package com.porvak.bracket.domain;

import static com.google.common.base.Preconditions.*;

public class Game extends AbstractBracket{

    private int gameId;
    private GameStatus status;
    private GameTeam[] teams;

    public Game() {
        teams = new GameTeam[2];
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public GameTeam[] getTeams() {
        return teams;
    }
    
    public GameTeam getTeamByPosition(int position){
        checkArgument(position >= 0 && position <= 1, "Team position must be 0 or 1. Received %s", position);
        return teams[position];
    }

    public void addTeam(GameTeam team){
        team = checkNotNull(team, "Team cannot be null");
        checkArgument(team.getPosition() >= 0 && team.getPosition() <= 1, "Team position must be 0 or 1. Received %s", team.getPosition());
        teams[team.getPosition()] =  team;
    }

}
