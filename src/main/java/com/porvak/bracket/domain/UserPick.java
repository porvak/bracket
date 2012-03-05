package com.porvak.bracket.domain;

import com.google.common.collect.Maps;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;

import java.util.Map;

import static com.google.common.base.Preconditions.*;

public class UserPick extends AbstractBracket{

    @JsonProperty
    private int regionId;

    @JsonProperty
    private int gameId;

    @JsonProperty
    private String teamId;
    
    @Transient
    private Map<String, Integer> userPickKey;

    @PersistenceConstructor
    public UserPick(@JsonProperty("regionId") int regionId, @JsonProperty("gameId") int gameId, @JsonProperty("teamId") String teamId) {
        init(regionId, gameId, teamId);
    }

    private void init(int regionId, int gameId, String teamId) {
        this.regionId = regionId;
        this.gameId = gameId;
        this.teamId = teamId;

        userPickKey = Maps.newHashMap();
        userPickKey.put("regionId", regionId);
        userPickKey.put("gameId", gameId);
    }

    /**
     * Create a userPick from a map.
     * 
     * @param userPickMap
     */
    public UserPick(Map<String, Object> userPickMap) {
        int regionId = Integer.valueOf(checkNotNull(userPickMap.get("regionId")).toString());
        int gameId = Integer.valueOf(checkNotNull(userPickMap.get("gameId")).toString());
        String teamId = checkNotNull(userPickMap.get("teamId")).toString();
        init(regionId, gameId, teamId);
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
