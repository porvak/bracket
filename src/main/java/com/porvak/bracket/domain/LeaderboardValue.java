package com.porvak.bracket.domain;

public class LeaderboardValue extends AbstractBracket{
    int score;
    int available;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}
