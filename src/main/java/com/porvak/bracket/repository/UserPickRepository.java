package com.porvak.bracket.repository;

import com.porvak.bracket.domain.UserPick;

import java.util.Map;

public interface UserPickRepository {

    void updateUserPick(String userId, String poolId, UserPick userPick);

    void addTieBreaker(String userId, String poolId, int tieBreaker);

    void removeUserPick(String userId, String poolId, int regionId, int gameId, int position);

    void updatePreviousGame(String userId, String poolId, Map<String, Object> userPickMap);

    void addGameWinner(String userId, String poolId, Map<String, Object> userPickMap);
}
