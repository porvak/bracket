package com.porvak.bracket.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Used as a pointer to the next/previous game.
 */
public class GamePointer {
    
    @JsonProperty
    private int regionId;

    @JsonProperty
    private int gameId;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private Integer position;

    public GamePointer() {
    }

    public GamePointer(@JsonProperty("regionId") int regionId, @JsonProperty("gameId") int gameId, @JsonProperty("position") Integer position) {
        Game.validatePosition(position);
        this.regionId = regionId;
        this.gameId = gameId;
        this.position = position;
    }

    public int getRegionId() {
        return regionId;
    }

    public int getGameId() {
        return gameId;
    }

    public int getPosition() {
        return position;
    }
}
