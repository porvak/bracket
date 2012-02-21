package com.porvak.bracket.domain;

public class Leaderboard extends AbstractBracket{
    private String id;
    private String poolId;
    private String userId;
    private String username;
    private Integer score;
    private Integer availablePoints;

    public Leaderboard() {
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getAvailablePoints() {
        return availablePoints;
    }

    public void setAvailablePoints(Integer availablePoints) {
        this.availablePoints = availablePoints;
    }
}
