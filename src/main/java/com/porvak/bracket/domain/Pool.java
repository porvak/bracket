package com.porvak.bracket.domain;

import java.util.Map;

public class Pool extends AbstractBracket{
    private String id;
    private String poolName;
    private String tournamentId;
    private PoolType poolType;
    Map<Integer, Integer> scoringStrategy;

    public Map<Integer, Integer> getScoringStrategy() {
        return scoringStrategy;
    }

    public void setScoringStrategy(Map<Integer, Integer> scoringStrategy) {
        this.scoringStrategy = scoringStrategy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public PoolType getPoolType() {
        return poolType;
    }

    public void setPoolType(PoolType poolType) {
        this.poolType = poolType;
    }
}
