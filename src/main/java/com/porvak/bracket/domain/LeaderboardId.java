package com.porvak.bracket.domain;

import org.springframework.data.annotation.PersistenceConstructor;

public class LeaderboardId extends AbstractBracket{
    String userId;
    String poolId;
    String tournamentId;
    int roundId;

    @PersistenceConstructor
    public LeaderboardId(String userId, String poolId, String tournamentId, int roundId) {
        this.userId = userId;
        this.poolId = poolId;
        this.tournamentId = tournamentId;
        this.roundId = roundId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPoolId() {
        return poolId;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public int getRoundId() {
        return roundId;
    }
}
