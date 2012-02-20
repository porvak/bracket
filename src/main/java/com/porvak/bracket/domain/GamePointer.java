package com.porvak.bracket.domain;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Used as a pointer to the next/previous game.
 */
public class GamePointer {
    
    @JsonProperty
    private int regionId;

    @JsonProperty
    private int gameId;

    @JsonProperty
    private int position;

    public GamePointer() {
    }

    public GamePointer(@JsonProperty("regionId") int regionId, @JsonProperty("gameId") int gameId, @JsonProperty("position") int position) {
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
