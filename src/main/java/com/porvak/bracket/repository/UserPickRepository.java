package com.porvak.bracket.repository;

import com.porvak.bracket.domain.UserPick;

public interface UserPickRepository {

    void updateUserPick(String userId, String poolId, UserPick userPick);
}
