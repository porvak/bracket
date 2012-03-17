package com.porvak.bracket.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.Map;

import static com.google.common.collect.Maps.*;

public class Leaderboard extends AbstractBracket{
    private String id;
    private String poolId;
    private String userId;
    private String username;
    private Integer totalScore;
    private Integer totalAvailablePoints;

    @JsonProperty
    private Map<Integer, RoundScore> roundScores;

    @PersistenceConstructor
    public Leaderboard() {
        roundScores = newHashMap();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getTotalAvailablePoints() {
        return totalAvailablePoints;
    }

    public void setTotalAvailablePoints(Integer totalAvailablePoints) {
        this.totalAvailablePoints = totalAvailablePoints;
    }

    public void addRoundScore(int roundId, int score, int available) {
        roundScores.put(roundId, new RoundScore(score, available));
        totalScore = 0;
        totalAvailablePoints = 0;
        for (Map.Entry<Integer, RoundScore> roundScoresEntry : roundScores.entrySet()) {
            totalScore = totalScore + roundScoresEntry.getValue().getScore();
            totalAvailablePoints = totalAvailablePoints + roundScoresEntry.getValue().getAvailablePoints();
        }
    }
}
