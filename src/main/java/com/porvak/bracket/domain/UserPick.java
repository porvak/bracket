package com.porvak.bracket.domain;

import com.google.common.collect.Maps;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.annotation.Transient;

import java.util.Map;

public class UserPick extends AbstractBracket{

    @JsonProperty
    private int regionId;

    @JsonProperty
    private int gameId;

    @JsonProperty
    private String teamId;
    
    @Transient
    private Map<String, Integer> userPickKey;

    public UserPick(@JsonProperty("regionId") int regionId, @JsonProperty("gameId") int gameId, @JsonProperty("teamId") String teamId) {
        this.regionId = regionId;
        this.gameId = gameId;
        this.teamId = teamId;

        userPickKey = Maps.newHashMap();
        userPickKey.put("regionId", regionId);
        userPickKey.put("gameId", gameId);
    }
    
    @JsonIgnore
    public Map<String, Integer> getKey(){
        return userPickKey;
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
