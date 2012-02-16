package com.porvak.bracket.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class UserPick extends AbstractBracket{

    @JsonProperty
    private int regionId;

    @JsonProperty
    private int gameId;

    @JsonProperty
    private String teamId;

    public UserPick(@JsonProperty("regionId") int regionId, @JsonProperty("gameId") int gameId, @JsonProperty("teamId") String teamId) {
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
