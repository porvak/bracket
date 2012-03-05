package com.porvak.bracket.service;

import com.porvak.bracket.domain.UserPick;

public interface PoolService {
    void addUserPick(String userId, String poolId, UserPick userPick);
}
