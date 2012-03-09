package com.porvak.bracket.repository;

import com.porvak.bracket.domain.UserPick;

public interface UserPickRepository {

    void updateUserPick(String userId, String poolId, UserPick userPick);

    void addTieBreaker(String userId, String poolId, int tieBreaker);

    void removeUserPick(String userId, String poolId, int regionId, int gameId, int position);
}
