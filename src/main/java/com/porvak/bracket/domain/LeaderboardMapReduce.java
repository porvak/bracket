package com.porvak.bracket.domain;

import org.springframework.data.annotation.Id;

public class LeaderboardMapReduce extends AbstractBracket{
//        { "_id" : { "userId" : "4f5defcce4b0ecbc95a69f23", "poolId" : "4f3c8297a0eea26b78d77538", "tournamentId" : "4f41ce03d17060d0d8dbd4d6", "roundId" : 1 },
// "value" : { "score" : 0, "available" : 32 } }
    @Id
    private LeaderboardId id;

    private LeaderboardValue value;

    public LeaderboardId getId() {
        return id;
    }

    public void setId(LeaderboardId id) {
        this.id = id;
    }

    public LeaderboardValue getValue() {
        return value;
    }

    public void setValue(LeaderboardValue value) {
        this.value = value;
    }
}
