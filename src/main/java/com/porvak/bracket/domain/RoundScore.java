package com.porvak.bracket.domain;

class RoundScore {
    private final int score;
    private final int availablePoints;

    RoundScore(final int score, final int availablePoints) {
        this.score = score;
        this.availablePoints = availablePoints;
    }

    public int getScore() {
        return score;
    }

    public int getAvailablePoints() {
        return availablePoints;
    }
}
