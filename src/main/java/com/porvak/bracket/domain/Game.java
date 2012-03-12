package com.porvak.bracket.domain;

import com.google.common.collect.Maps;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

import static com.google.common.base.Preconditions.*;

public class Game extends AbstractBracket implements Comparable<Game>{

    private int gameId;
    private GameStatus status;

    @JsonIgnore
    @Field("teams")
    private Map<Integer, GameTeam> teamMap;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private Team userGameWinner;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private GamePointer nextGame;

    public Game() {
        teamMap = Maps.newLinkedHashMap();
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
        GameTeam[] teamArray = new GameTeam[2];
        teamArray[0] = teamMap.get(0);
        teamArray[1] = teamMap.get(1);
        return teamArray;
    }
    
    public void setNextGame(GamePointer nextGame){
        this.nextGame = nextGame;
    }

    public GamePointer getNextGame() {
        return nextGame;
    }

    public GameTeam getTeamByPosition(int position){
        validatePosition(position);
        return teamMap.get(position);
    }

    public void addTeam(GameTeam team){
        team = checkNotNull(team, "Team cannot be null");
        validatePosition(team.getPosition());
        teamMap.put(team.getPosition(), team);
    }

    /**
     * Returns the winning team
     *
     * @return null if no team has won.
     */
    @JsonIgnore
    public GameTeam getWinningTeam(){
        GameTeam team0 = teamMap.get(0);
        GameTeam team1 = teamMap.get(1);
        if(team0 != null && team0.isWinner()){
            return team0;
        }
        else if(team1 != null && team1.isWinner()){
            return team1;
        }
        
        return null;
    }

    public Team getUserGameWinner() {
        return userGameWinner;
    }

    public void setUserGameWinner(Team userGameWinner) {
        this.userGameWinner = userGameWinner;
    }

    public static void validatePosition(int position){
        checkArgument(position == 0 || position == 1, "Team position must be 0 or 1. Received %s", position);
    }

    public Map<Integer, GameTeam> getTeamMap() {
        return teamMap;
    }

    public void setTeamMap(Map<Integer, GameTeam> teamMap) {
        this.teamMap = teamMap;
    }

    @Override
    public int compareTo(Game o) {
        if(gameId > o.getGameId())
            return 1;
        else if(gameId < o.getGameId())
            return -1;
        else
            return 0;
    }
}
