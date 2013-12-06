package com.porvak.bracket.service;

import java.util.Map;

public interface PoolService {
    void addUserPick(String userId, String poolId, Map<String, Object> userPick);

    void addTieBreaker(String userId, String poolId, int tieBreaker);

    void removeUserPick(String userId, String poolId, int regionId, int gameId, int position);
}
