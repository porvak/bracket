package com.porvak.bracket.domain;

public class UserPick extends AbstractBracket{
    private int regionId;
    private int gameId;
    private String teamId;

    public UserPick(int regionId, int gameId, String teamId) {
        this.regionId = regionId;
        this.gameId = gameId;
        this.teamId = teamId;
    }

    public int getRegionId() {
        return regionId;
    }

    public int getGameId() {
        return gameId;
    }

    public String getTeamId() {
        return teamId;
    }
}
